package app.quiz.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuizRepository {

	private static final String API_URL = 
			"https://script.googleusercontent.com/macros/echo?user_content_key="
			+ "AUkAhnQdq5c5lGyhqkQteGjilxzoCsA26aQ7AoyAvYj1va6CInAX9YW-_mrz_a_3"
			+ "aM91byTNCpBdTDYyVJ-J0cjUtXQfbi7xVf366nwtnqXlpLVJIf1dLSF7EG0KS4IjH"
			+ "G1EBSK8sVmjFbsc7sJifp84tuguhxwlq7-VsRGDUvmezpIv91tB78y8UbBAYsAKk"
			+ "TMNzLfB1T_yKtNpb-43Et2p9bzP8bwF8D3o-8U-3Hf3kRrcY8Gi2wdHmhMt5Wu"
			+ "qGT6OwEcGmE-KA3bqzfN4NY_O68LVAwcZOw&lib=MZNZgYpjcB1BIne1bWlFDl5e5J8QAxtqO";
	/**
	 * クイズデータを非同期（別スレッド）で通信し、読み込みます。
	 * @param listener 通信完了時に結果を受け取るリスナーオブジェクト
	 */
	public void loadQuizzesAsync(final OnQuizLoadListener listener) {

		// ラムダ式を使わず、Runnableインターフェースを匿名クラスで実装
		Runnable runCommunication = new Runnable() {
			@Override
			public void run() {
				try {
					// HTTPS通信の準備（クライアントとリクエストの作成）
					HttpClient client = HttpClient.newBuilder()
							.followRedirects(HttpClient.Redirect.ALWAYS)
                            .build();
					HttpRequest request = HttpRequest.newBuilder()
							.uri(URI.create(API_URL))
							.GET() // データを取得するGETモード
							.build();

					// 実際に通信を実行し、結果を文字列（JSON）として受け取る
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
					String json = response.body();

					// 3. Gsonを使って、届いたJSON文字列を List<Quiz> に変換する
					Gson gson = new Gson();
					java.lang.reflect.Type listType = new TypeToken<ArrayList<Quiz>>() {}.getType();
					
					List<Quiz> quizzes = gson.fromJson(json, listType);

					// 4. 通信と変換が成功したら、待たせているメイン側に通知する（コールバック）
					if (listener != null) {
						listener.onSuccess(quizzes);
					}

				} catch (Exception e) {
					// ネットワークエラー、URL間違い、Gsonの変換失敗などはすべてここへ飛ぶ
					if (listener != null) {
						listener.onFailure(e);
					}
				}
			}
		};

		// 新しい作業員（スレッド）を立ち上げて、通信処理を裏でスタートさせる
		Thread thread = new Thread(runCommunication);
		thread.start();
	}
}
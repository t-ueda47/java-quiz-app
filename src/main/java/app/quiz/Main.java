package app.quiz;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import app.quiz.model.OnQuizLoadListener;
import app.quiz.model.Quiz;
import app.quiz.model.QuizRepository;
import app.quiz.util.AppLogger; // 💡 作成した管理クラスをインポート

public class Main {
	public static void main(String[] args) {
		AppLogger.print("クイズデータの読み込みを開始します...");

		QuizRepository repository = new QuizRepository();

		repository.loadQuizzesAsync(new OnQuizLoadListener() {
			@Override
			public void onSuccess(List<Quiz> quizzes) {
				AppLogger.print("通信成功！全 " + quizzes.size() + " 問を読み込みました。\n");

				Collections.shuffle(quizzes);
				int limit = Math.min(5, quizzes.size());
				List<Quiz> selectedQuizzes = quizzes.subList(0, limit);

				Scanner scanner = new Scanner(System.in);
				int correctCount = 0;

				AppLogger.print("====== クイズスタート（全 " + limit + " 問） ======");

				for (int i = 0; i < selectedQuizzes.size(); i++) {
					Quiz q = selectedQuizzes.get(i);

					// 💡 すべて AppLogger 経由の出力に差し替え
					AppLogger.print("【第" + (i + 1) + "問】 " + q.getQuestion());
					AppLogger.print("   1: " + q.getChoice1() + " / 2: " + q.getChoice2() + " / 3: " + q.getChoice3()
							+ " / 4: " + q.getChoice4());
					AppLogger.printSameLine("👉 正解の番号（1〜4）を入力してください: ");

					String input = scanner.nextLine();

					try {
						int userAnswer = Integer.parseInt(input);
						if (userAnswer == q.getAnswer()) {
							correctCount++;
						}
					} catch (NumberFormatException e) {
						// エラーログを出さないため、キャッチしても何もしない（あるいはAppLoggerに逃がす）
					}
					AppLogger.print("");
				}

				AppLogger.print("====== クイズ終了 ======");
				AppLogger.print("あなたの正解数は... 【 " + correctCount + " / " + limit + " 】でした！");

				scanner.close();
			}

			@Override
			public void onFailure(Exception e) {
				AppLogger.print("通信失敗...");
				AppLogger.error(e); // 💡 フラグ連動のエラー出力
			}
		});

		AppLogger.print("Mainメソッドの処理はここで終わりです（通信は裏で続いています）。");
	}
}
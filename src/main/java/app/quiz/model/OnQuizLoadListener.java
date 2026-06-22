package app.quiz.model;

import java.util.List;

/**
 * コールバックインターフェイス
 */
public interface OnQuizLoadListener {
	
	/**	通信成功時に呼び出すメソッド	*/
	public void onSuccess(List<Quiz> quizzes);
	
	/**	通信失敗時に呼び出すメソッド	*/
	public void onFailure(Exception e);
	
}

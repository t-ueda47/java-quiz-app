package app.quiz.model;

/**
 * スプレッドシートから読み込んだデータを格納
 */
public class Quiz {

	/**	問題			*/
	private String question;
	/**	選択肢1～4		*/
	private String choice1;
	private String choice2;
	private String choice3;
	private String choice4;
	/**	答え		*/
	private int answer; 
	
	public Quiz() {}

	public Quiz(String question, String choice1, String choice2, 
			String choice3, String choice4, int answer) {
		this.question = question;
		this.choice1 = choice1;
		this.choice2 = choice2;
		this.choice3 = choice3;
		this.choice4 = choice4;
		this.answer = answer;
	}

	public String getQuestion() {return question;}

	public String getChoice1() {return choice1;}

	public String getChoice2() {return choice2;}

	public String getChoice3() {return choice3;}

	public String getChoice4() {return choice4;}

	public int getAnswer() {return answer;}
	
}

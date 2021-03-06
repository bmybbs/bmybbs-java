package edu.xjtu.bmybbs.ythtbbs;

import edu.xjtu.bmybbs.Constants;

public class Article {
	private String board;
	private String title;
	private String owner;
	private String timestamp;
	private String thread;
	private String filename;

	public Article(String board, String title, String owner, long timestamp, long thread) {
		this(board, title, owner, Long.toString(timestamp), Long.toString(thread));
	}

	public Article(String board, String title, String owner, String timestamp, String thread) {
		this.board = board;
		this.title = title;
		this.owner = owner;
		this.timestamp = timestamp;
		this.thread = thread;
		this.filename = String.format("M.%s.A", timestamp);
	}

	public String getFilename() {
		return this.filename;
	}

	public String getOwner() {
		return this.owner;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public String getThread() {
		return thread;
	}

	public String getPath() {
		return String.format("%s/%s/%s", Constants.BOARDSPATH, this.board, this.filename);
	}

	public native String getContent();

	static {
		System.loadLibrary("bmybbs-java");
	}
}

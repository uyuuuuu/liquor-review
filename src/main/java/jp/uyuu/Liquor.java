package jp.uyuu;

public class Liquor {
	// 初期
	private String name, kind, company, info;
	// レビュー
	private int count, star;
	private boolean favorite;
	private String comment;

	public Liquor(String name, String kind, String company, String info) {
		this.name = name;
		this.kind = kind;
		this.company = company;
		this.info = info;

		this.count = 0;
		this.star = 0;
		this.comment = null;
		this.favorite = false;
	}

	public Liquor(String name, String kind, int count, String company, String info, int star, String comment,
			boolean favorite) {
		this.name = name;
		this.kind = kind;
		this.company = company;
		this.info = info;

		this.count = count;
		this.star = star;
		this.comment = comment;
		this.favorite = favorite;
	}

	// 名前
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ジャンル
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	// 会社名
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	// 情報
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	// 飲んだ回数
	public int getCount() {
		return count;
	}

	public void upCount() {
		this.count++;
	}

	// 星
	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	// お気に入り
	public boolean getFavorite() {
		return favorite;
	}

	public void changeFavorite() {
		this.favorite = !favorite;
	}

	// レビュー
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		String tmp = name + "," + kind + "," + count + "," + company + "," + info;
		String tmp2 = "," + star + "," + comment + "," + favorite;
		String result = tmp + tmp2;
		return result;
	}
}

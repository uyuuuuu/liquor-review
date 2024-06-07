package jp.uyuu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Page {
	private ArrayList<Liquor> list;

	public Page() {
		list = new ArrayList<Liquor>();
	}

	public void open(String filename) { // ファイルからの読み込み
		try {
			list.clear();
			File file = new File(filename);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] field = line.split(",");
				for (int i = 0; i < field.length; i++) {
					if (field[i].equals("null"))
						field[i] = null;
				}
				boolean fav = false;
				if (field[7].equals("true"))
					fav = true;
				Liquor li = new Liquor(field[0], field[1], Integer.parseInt(field[2]), field[3], field[4],
						Integer.parseInt(field[5]), field[6], fav);
				add(li);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String filename) { // ファイルへの書き出し
		try {
			File file = new File(filename);
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			for (Liquor liquor : list) {
				writer.println(liquor);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add(Liquor liquor) { // お酒の追加
		list.add(liquor);
	}

	public void remove(Liquor liquor) { // お酒の削除
		list.remove(liquor);
	}

	public void showLiquors() { // 一覧をプリント文で表示
		for (Liquor liquor : list) {
			System.out.println(liquor);
		}
	}

	public Liquor findName(String name) { // お酒の名前でデータを探す
		for (Liquor liquor : list) {
			if (name.equals(liquor.getName())) {
				return liquor;
			}
		}
		return null;
	}

	public ArrayList<String> getNames() { // 名前の一覧を得る
		ArrayList<String> nameList = new ArrayList<String>();
		for (Liquor liquor : list) {
			nameList.add(liquor.getName());
		}
		return nameList;
	}
}
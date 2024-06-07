package jp.uyuu;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Report extends JFrame {
	JTextField nameField, companyField, infoField, commentField, filterField;
	DefaultListModel model;
	JList list;
	JComboBox<String> filterCB, favCB, kindCB;
	JCheckBoxMenuItem favoriteButton;
	ButtonGroup starButtons;
	JButton countButton, createButton, addButton, removeButton, updateButton, searchButton, tweetButton;
	JPanel pane;
	Page page;
	JMenuItem st1, st2, st3, st4, st5;

	public static void main(String[] args) {
		JFrame w = new Report("飲んだよデータベース");
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setMinimumSize(new Dimension(800, 500));
		w.setIconImage(new ImageIcon("./liquorIcon.png").getImage());
		w.setSize(800, 500);
		w.setVisible(true);
	}

	public Report(String title) {
		super(title);
		page = new Page();
		pane = (JPanel) getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("ファイル");
		menuBar.add(fileMenu);
		// メニューバー - 開く、保存、終了
		JMenuItem item;
		item = new JMenuItem(new OpenAction());
		fileMenu.add(item);
		item = new JMenuItem(new SaveAction());
		fileMenu.add(item);
		fileMenu.addSeparator();
		item = new JMenuItem(new ExitAction());
		fileMenu.add(item);

		// 左側パネル
		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());

		// フィルタ機能
		JPanel filterPane = new JPanel();
		filterPane.setLayout(new BoxLayout(filterPane, BoxLayout.Y_AXIS));
		filterField = new JTextField(20);
		filterField.setBorder(new TitledBorder("検索"));
		filterField.addActionListener(new FilterSelect());
		filterPane.add(filterField);

		filterCB = new JComboBox<String>();
		filterCB.addActionListener(new FilterSelect());
		filterCB.addItem("全種類");
		filterCB.addItem("ビール");
		filterCB.addItem("チューハイ");
		filterCB.addItem("日本酒");
		filterCB.addItem("ワイン");
		filterCB.addItem("焼酎");
		filterCB.addItem("ウイスキー");
		filterCB.addItem("蒸留酒");
		filterCB.addItem("リキュール");
		filterCB.addItem("果実酒");
		filterCB.addItem("その他");
		filterCB.setAlignmentX(0.5f);
		filterPane.add(filterCB);

		favCB = new JComboBox<String>();
		favCB.addActionListener(new FilterSelect());
		favCB.addItem("お気に入りフィルタなし");
		favCB.addItem("お気に入りのみ");
		favCB.setAlignmentX(0.5f);
		filterPane.add(favCB);
		listPane.add(filterPane, BorderLayout.NORTH);

		// 一覧
		model = new DefaultListModel();
		list = new JList(model);
		list.addListSelectionListener(new NameSelect());
		JScrollPane sc = new JScrollPane(list);
		sc.setBorder(new TitledBorder("名前一覧"));
		listPane.add(sc, BorderLayout.CENTER);

		// ボタン
		JPanel bPane = new JPanel();
		bPane.setLayout(new BoxLayout(bPane, BoxLayout.Y_AXIS));
		// 検索ボタン
		searchButton = new JButton(new SearchAction());
		searchButton.setAlignmentX(0.5f);
		bPane.add(searchButton);
		// ツイート
		tweetButton = new JButton(new TweetAction());
		tweetButton.setAlignmentX(0.5f);
		bPane.add(tweetButton);
		listPane.add(bPane, BorderLayout.SOUTH);

		pane.add(listPane);

		// 入力欄
		JPanel fields = new JPanel();
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

		nameField = new JTextField(20);
		nameField.setBorder(new TitledBorder("名前"));
		fields.add(nameField);

		kindCB = new JComboBox<String>();
		// プルダウン
		kindCB.addItem("種類を選択");
		kindCB.addItem("ビール");
		kindCB.addItem("チューハイ");
		kindCB.addItem("日本酒");
		kindCB.addItem("ワイン");
		kindCB.addItem("焼酎");
		kindCB.addItem("ウイスキー");
		kindCB.addItem("蒸留酒");
		kindCB.addItem("リキュール");
		kindCB.addItem("果実酒");
		kindCB.addItem("その他");
		fields.add(kindCB);
		companyField = new JTextField(20);
		companyField.setBorder(new TitledBorder("製造"));
		fields.add(companyField);
		infoField = new JTextField(20);
		infoField.setBorder(new TitledBorder("情報"));
		fields.add(infoField);
		commentField = new JTextField(20);
		commentField.setBorder(new TitledBorder("レビュー"));
		fields.add(commentField);

		// 新規,登録,更新,削除
		JPanel aurPane = new JPanel();
		createButton = new JButton(new CreateAction());
		aurPane.add(createButton);
		addButton = new JButton(new AddAction());
		aurPane.add(addButton);
		updateButton = new JButton(new UpdateAction());
		aurPane.add(updateButton);
		removeButton = new JButton(new RemoveAction());
		aurPane.add(removeButton);
		fields.add(aurPane);
		// ラジオボタン - 星
		starButtons = new ButtonGroup();
		JPanel starPane = new JPanel();
		st1 = new JRadioButtonMenuItem(new StarAction("☆", 1));
		starPane.add(st1);
		starButtons.add(st1);
		st2 = new JRadioButtonMenuItem(new StarAction("☆☆", 2));
		starPane.add(st2);
		starButtons.add(st2);
		st3 = new JRadioButtonMenuItem(new StarAction("☆☆☆", 3));
		starPane.add(st3);
		starButtons.add(st3);
		st4 = new JRadioButtonMenuItem(new StarAction("☆☆☆☆", 4));
		starPane.add(st4);
		starButtons.add(st4);
		st5 = new JRadioButtonMenuItem(new StarAction("☆☆☆☆☆", 5));
		starPane.add(st5);
		starButtons.add(st5);
		fields.add(starPane);

		// ボタン
		countButton = new JButton(new CountAction());
		countButton.setAlignmentX(0.5f);
		fields.add(countButton);
		favoriteButton = new JCheckBoxMenuItem(new FavAction());
		fields.add(favoriteButton);

		pane.add(fields);
		// 初期設定
		allEnabled(false);
	}

	// 選択したら
	class NameSelect implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			Object obj = list.getSelectedValue();
			if (obj != null) {
				Liquor select = page.findName(obj.toString());
				nameField.setText(select.getName());
				kindCB.setSelectedItem(select.getKind());

				String comp = select.getCompany();
				if (comp == null)
					comp = "";
				companyField.setText(comp);

				String inf = select.getInfo();
				if (inf == null)
					inf = "";
				infoField.setText(inf);

				String comment = select.getComment();
				if (comment == null)
					comment = "";
				commentField.setText(comment);
				// 制限解除
				enableStarButton(true);
				favoriteButton.setEnabled(true);
				countButton.setEnabled(true);
				// 星
				int star = select.getStar();
				switch (star) {
					case 1:
						st1.setSelected(true);
						break;
					case 2:
						st2.setSelected(true);
						break;
					case 3:
						st3.setSelected(true);
						break;
					case 4:
						st4.setSelected(true);
						break;
					case 5:
						st5.setSelected(true);
						break;
					default:
						starButtons.clearSelection();
						break;
				}

				// 飲んだ回数
				countButton.setText("飲んだよカウンター:" + select.getCount() + "杯目");
				// お気に入り状況
				favoriteButton.setSelected(select.getFavorite());
				allEnabled(true);
				addButton.setEnabled(false);
			}
		}
	}

	// フィルタープルダウン
	class FilterSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (model == null)
				return;
			String word = filterField.getText();
			String target = (String) filterCB.getSelectedItem();
			model.clear();
			ArrayList<String> nameList = page.getNames();
			if (filterCB.getSelectedIndex() == 0 && favCB.getSelectedIndex() == 0) {
				for (String name : nameList) {
					if (name.contains(word))
						model.addElement(name);
				}
			} else if (filterCB.getSelectedIndex() == 0) { // favのみ
				for (String name : nameList) {
					if (page.findName(name).getFavorite()) {
						if (name.contains(word))
							model.addElement(name);
					}
				}
			} else if (favCB.getSelectedIndex() == 0) { // 種類のみ
				for (String name : nameList) {
					if (page.findName(name).getKind().equals(target)) {
						if (name.contains(word))
							model.addElement(name);
					}
				}
			} else {
				for (String name : nameList) { // 両方
					if (page.findName(name).getKind().equals(target) && page.findName(name).getFavorite()) {
						if (name.contains(word))
							model.addElement(name);
					}
				}
			}
			allEnabled(false);
		}
	}

	// メニューバー
	class OpenAction extends AbstractAction {
		OpenAction() {
			putValue(Action.NAME, "開く");
			putValue(Action.SHORT_DESCRIPTION, "開く");
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(new File("").getAbsolutePath()); // カレントディレクトリを指定してファイルチューザを生成
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // モードを設定
			fileChooser.setDialogTitle("データファイルを選択"); // タイトルを指定

			int ret = fileChooser.showOpenDialog(pane); // ダイアログを開く

			if (ret != JFileChooser.APPROVE_OPTION)
				return; // 選ばれていなければ

			String fileName = fileChooser.getSelectedFile().getAbsolutePath(); // 選ばれていればそのファイルのパスを得る
			// ファイル読み込み
			page.open(fileName);
			// リストに入れる
			ArrayList<String> nameList = page.getNames();
			model.clear(); // クリア
			for (String name : nameList) {
				model.addElement(name);
			}
			resetField();
			allEnabled(false);
		}
	}

	class SaveAction extends AbstractAction {
		SaveAction() {
			putValue(Action.NAME, "保存");
			putValue(Action.SHORT_DESCRIPTION, "保存");
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("."); // カレントディレクトリを指定してファイルチューザを生成
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // モードを設定
			fileChooser.setDialogTitle("データファイルを選択"); // タイトルを指定
			fileChooser.setApproveButtonText("保存");
			fileChooser.setApproveButtonToolTipText("選択したファイルに保存します");

			int ret = fileChooser.showOpenDialog(pane); // ダイアログを開く

			if (ret != JFileChooser.APPROVE_OPTION)
				return; // 選ばれていなければ

			String fileName = fileChooser.getSelectedFile().getAbsolutePath(); // 選ばれていればそのファイルのパスを得る
			page.save(fileName);
		}
	}

	class ExitAction extends AbstractAction {
		ExitAction() {
			putValue(Action.NAME, "終了");
			putValue(Action.SHORT_DESCRIPTION, "終了");
		}

		public void actionPerformed(ActionEvent e) {
			int ans = JOptionPane.showConfirmDialog(Report.this, "本当に終了しますか？"); // ダイアログを表示して結果を得る
			if (ans == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	// 特殊ボタン
	class StarAction extends AbstractAction {
		private int star;

		StarAction(String name, int star) {
			putValue(Action.NAME, name);
			putValue(Action.SHORT_DESCRIPTION, "星評価");
			this.star = star;
		}

		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			DefaultListModel model = (DefaultListModel) list.getModel();
			if (name.equals("") || kindCB.getSelectedIndex() == 0)
				return;
			// 更新
			Liquor li = page.findName(name);
			li.setStar(star);
			page.showLiquors();
		}
	}

	class FavAction extends AbstractAction {
		FavAction() {
			putValue(Action.NAME, "お気に入り");
			putValue(Action.SHORT_DESCRIPTION, "お気に入り登録/解除");
		}

		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			DefaultListModel model = (DefaultListModel) list.getModel();
			if (name.equals("") || kindCB.getSelectedIndex() == 0)
				return;
			// 更新
			Liquor li = page.findName(name);
			li.changeFavorite();
			page.showLiquors();
		}
	}

	class CountAction extends AbstractAction {
		CountAction() {
			putValue(Action.NAME, "飲んだよカウンター:0杯目");
			putValue(Action.SHORT_DESCRIPTION, "飲んだ回数を増やす");
		}

		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			String kind = (String) kindCB.getSelectedItem();
			DefaultListModel model = (DefaultListModel) list.getModel();

			if (name.equals("") || kindCB.getSelectedIndex() == 0)
				return;
			// 更新
			Liquor li = page.findName(name);
			li.upCount();
			putValue(Action.NAME, "飲んだよカウンター:" + li.getCount() + "杯目");
			page.showLiquors();
		}
	}

	class SearchAction extends AbstractAction {
		SearchAction() {
			putValue(Action.NAME, "検索");
			putValue(Action.SHORT_DESCRIPTION, "このお酒を検索");
		}

		public void actionPerformed(ActionEvent e) {
			Object obj = list.getSelectedValue();
			if (obj == null)
				return;
			Liquor select = page.findName(obj.toString());
			String query = select.getName();
			Desktop desktop = Desktop.getDesktop();
			try {
				query = URLEncoder.encode(query, "UTF-8");
				String uriString = "https://www.google.co.jp/search?q=" + query;
				try {
					URI uri = new URI(uriString);
					desktop.browse(uri);
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				popDialog("検索名称を確認してください。");
			}
		}
	}

	class TweetAction extends AbstractAction {
		TweetAction() {
			putValue(Action.NAME, "飲んだよツイート");
			putValue(Action.SHORT_DESCRIPTION, "このお酒についてXでツイート");
		}

		public void actionPerformed(ActionEvent e) {
			Object obj = list.getSelectedValue();
			if (obj == null)
				return;
			Liquor select = page.findName(obj.toString());

			String tweet = "";
			String nameStr = select.getName() + "を飲んだよ！";
			tweet += nameStr;
			if (select.getStar() > 0) {
				String stars = "";
				for (int i = 0; i < select.getStar(); i++)
					stars += "☆";
				String starStr = "\n評価：" + stars;
				tweet += starStr;
			}
			if (select.getComment() != null) {
				String comment = select.getComment();
				tweet += "\nレビュー：" + comment;
			}
			tweet += "\n#飲んだよLog";
			Desktop desktop = Desktop.getDesktop();
			try {
				tweet = URLEncoder.encode(tweet, "UTF-8");
				String uriString = "https://twitter.com/intent/tweet?&text=" + tweet;
				try {
					URI uri = new URI(uriString);
					desktop.browse(uri);
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				popDialog("検索名称を確認してください。");
			}
		}
	}

	// ボタン
	class CreateAction extends AbstractAction {
		CreateAction() {
			putValue(Action.NAME, "新規");
			putValue(Action.SHORT_DESCRIPTION, "新しいお酒の情報を入力");
		}

		public void actionPerformed(ActionEvent e) {
			list.clearSelection();

			resetField();
			allEnabled(false);
			kindCB.setEnabled(true);
			nameField.setEnabled(true);
			companyField.setEnabled(true);
			infoField.setEnabled(true);
			addButton.setEnabled(true);
		}
	}

	class AddAction extends AbstractAction {
		AddAction() {
			putValue(Action.NAME, "登録");
			putValue(Action.SHORT_DESCRIPTION, "新しいお酒を登録");
		}

		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			String kind = (String) kindCB.getSelectedItem();
			String company = companyField.getText();
			String info = infoField.getText();

			DefaultListModel model = (DefaultListModel) list.getModel();

			if (name.contains(",") || kind.contains(",") || company.contains(",") || info.contains(",")) {
				popDialog("「,」を含まないようにしてください。"); // 「,」のエラー回避
				return;
			}
			if (model.contains(name)) {
				popDialog("このデータはすでに存在します。"); // 既に含まれている名前
				return;
			}
			if (name.equals("") || kindCB.getSelectedIndex() == 0) {
				popDialog("名前・種類のフィールドを埋めてください。"); // いずれかのテキストフィールドが空
				return;
			}

			if (company.equals(""))
				company = null;
			if (info.equals(""))
				info = null;
			page.add(new Liquor(name, kind, company, info)); // 辞書登録

			model.addElement(name); // GUIに追加
			int pos = model.getSize() - 1;
			list.ensureIndexIsVisible(pos);

			resetField();
			allEnabled(false);
		}
	}

	class UpdateAction extends AbstractAction {
		UpdateAction() {
			putValue(Action.NAME, "更新");
			putValue(Action.SHORT_DESCRIPTION, "更新");
		}

		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			String kind = (String) kindCB.getSelectedItem();
			String company = companyField.getText();
			if (company.equals(""))
				company = null;
			String info = infoField.getText();
			if (info.equals(""))
				info = null;
			String comment = commentField.getText();
			if (comment.equals(""))
				comment = null;

			DefaultListModel model = (DefaultListModel) list.getModel();

			if (name.contains(",") || kind.contains(",") || company.contains(",") || info.contains(",")) {
				popDialog("「,」を含まないようにしてください。"); // 「,」のエラー回避
				return;
			}
			if (!model.contains(name))
				return; // 既に含まれている名前でない
			if (name.equals("") || kindCB.getSelectedIndex() == 0) {
				popDialog("名前・種類のフィールドを埋めてください。"); // いずれかのテキストフィールドが空
				return;
			}

			// 更新
			Liquor li = page.findName(name);
			// kind,company,info,commentの更新
			li.setKind(kind);
			li.setCompany(company);
			li.setInfo(info);
			li.setComment(comment);
			page.showLiquors();
		}
	}

	class RemoveAction extends AbstractAction {
		RemoveAction() {
			putValue(Action.NAME, "削除");
			putValue(Action.SHORT_DESCRIPTION, "削除");
		}

		public void actionPerformed(ActionEvent e) {
			int index = list.getSelectedIndex();
			if (index < 0)
				return; // 選択していなかったらなにもしない

			DefaultListModel model = (DefaultListModel) list.getModel();
			String name = (String) model.get(index); // 選択した名前
			Object[] msg = { name, "を削除します" };
			// ダイアログ
			int ans = JOptionPane.showConfirmDialog(pane, msg, "はい・いいえ・取消し",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (ans == JOptionPane.YES_OPTION) { // はいを押したら
				model.remove(index); // GUIから削除
				page.remove(page.findName(name)); // 辞書から削除
				// リセット
				resetField();
				allEnabled(false);
			}
		}
	}

	// メソッド
	private void resetField() {
		nameField.setText("");
		kindCB.setSelectedIndex(0);
		companyField.setText("");
		infoField.setText("");
		commentField.setText("");
		favoriteButton.setSelected(false);
		starButtons.clearSelection();
	}

	private void enableStarButton(boolean tf) {
		st1.setEnabled(tf);
		st2.setEnabled(tf);
		st3.setEnabled(tf);
		st4.setEnabled(tf);
		st5.setEnabled(tf);
	}

	private void allEnabled(boolean tf) {
		// プルダウン
		kindCB.setEnabled(tf);
		// 星
		enableStarButton(tf);
		// お気に入り
		favoriteButton.setEnabled(tf);
		// 回数
		countButton.setEnabled(tf);
		// 入力欄
		nameField.setEnabled(tf);
		companyField.setEnabled(tf);
		infoField.setEnabled(tf);
		commentField.setEnabled(tf);
		// ボタン
		addButton.setEnabled(tf);
		removeButton.setEnabled(tf);
		updateButton.setEnabled(tf);
		searchButton.setEnabled(tf);
		tweetButton.setEnabled(tf);
	}

	private void popDialog(String msg) {
		// ダイアログ
		JOptionPane.showMessageDialog(pane, msg, "WARNING",
				JOptionPane.WARNING_MESSAGE);
	}
}

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Globalization;
using System.Threading;
using System.Collections;
using System.IO;

namespace WordListCreator
{
	public partial class MainForm : Form
	{
		private class WordCounter
		{
			private readonly string m_word;
			private int m_count;
			public WordCounter(string word)
			{
				m_word = word;
				m_count = 0;
			}

			public string Word { get { return m_word; } }
			public int Count { get { return m_count; } }

			public void Increment()
			{
				m_count++;
			}
		}

		public MainForm()
		{
			InitializeComponent();
			ClearOutputControls();
			txtBx_textToParse.MaxLength = int.MaxValue;
		}

		private void btn_parseText_Click(object sender, EventArgs e)
		{
			this.Enabled = false;
			this.lbl_parsingStatus.Text = "Parsing";
			ClearOutputControls();
			string textToParse = txtBx_textToParse.Text;
			Thread parsing = new Thread(Parser);
			parsing.Start(textToParse);
		}

		private void ClearOutputControls()
		{
			this.lstVw_wordList.Items.Clear();
			this.lbl_wordCount.Text = "0";
			this.lbl_bytesCount.Text = "--";
		}

		private void Parser(object state)
		{
			string textToParse = state.ToString();
			StringBuilder word = new StringBuilder();
			Dictionary<string, WordCounter> words = new Dictionary<string, WordCounter>();
			char[] chars = textToParse.ToCharArray();
			for (int charIndex = 0; charIndex < chars.Length; charIndex++)
			{
				char c = chars[charIndex];
				if (char.IsLetter(c))
					word.Append(c);
				else if ((c == '\'') && (word.Length > 0) && (char.IsLetter(chars[charIndex+1])))
				{//I'm is a word
					word.Append(c);
				}
				else
				{
					CountWord(word.ToString().ToLower(), words);
					word.Remove(0, word.Length);
				}
			}

			UpdateAllGuiData(words);
		}

		private void UpdateAllGuiData(Dictionary<string, WordCounter> words)
		{
			if (this.InvokeRequired)
			{
				this.BeginInvoke((ThreadStart)delegate() { UpdateAllGuiData(words); });
			}
			else
			{
				this.Enabled = true;
				this.lbl_parsingStatus.Text = "Done. Building GUI...";
				this.lbl_wordCount.Text = words.Count.ToString();
				int bytes = 0;
				lstVw_wordList.BeginUpdate();
				foreach (WordCounter word in words.Values)
				{
					bytes += word.Word.Length;
					ListViewItem item = new ListViewItem(new string[]{word.Word, word.Count.ToString()});
					lstVw_wordList.Items.Add(item);
				}
				lstVw_wordList.EndUpdate();
				this.lbl_parsingStatus.Text = "Done.";
				this.lbl_bytesCount.Text = bytes.ToString();
			}
		}

		private void CountWord(string aWord, Dictionary<string, WordCounter> words)
		{
			if (string.IsNullOrEmpty(aWord))
				return;
			WordCounter word;
			if (words.ContainsKey(aWord))
				word = words[aWord];
			else
			{
				word = new WordCounter(aWord);
				words.Add(aWord, word);
			}
			word.Increment();

			UpdateGUI(word, words);
		}

		private void UpdateGUI(WordCounter word, Dictionary<string, WordCounter> words)
		{
			if (this.InvokeRequired)
			{
				this.BeginInvoke((ThreadStart)delegate() { UpdateGUI(word, words); });
			}
			else
			{
				this.lbl_wordCount.Text = words.Count.ToString();
			}
		}

		private void btn_filter_Click(object sender, EventArgs e)
		{

		}

		private ColumnHeader m_sortingColumn = null;
		private void lstVw_wordList_ColumnClick(object sender, ColumnClickEventArgs e)
		{
			ColumnHeader column = lstVw_wordList.Columns[e.Column];
			SortOrder order;
			if (column == null)
				order = SortOrder.Ascending;
			else
			{
				if (column.Equals(m_sortingColumn))
				{
					if (column.Text.StartsWith("> "))
						order = SortOrder.Descending;
					else
						order = SortOrder.Ascending;
				}
				else
					order = SortOrder.Ascending;
			}

			if (m_sortingColumn != null)
				m_sortingColumn.Text = m_sortingColumn.Text.Substring(2);

			m_sortingColumn = column;
			if (order == SortOrder.Ascending)
				column.Text = "> " + column.Text;
			else
				column.Text = "< " + column.Text;

			lstVw_wordList.ListViewItemSorter = new ListViewComparer(e.Column, order);
			lstVw_wordList.Sort();
		}

		private class ListViewComparer : IComparer
		{
			private readonly int m_columnNumber;
			private readonly SortOrder m_order;

			public ListViewComparer(int columnNumber, SortOrder order)
			{
				m_columnNumber = columnNumber;
				m_order = order;
			}
			#region IComparer Members

			public int Compare(object x, object y)
			{
				ListViewItem item_x = (ListViewItem)x;
				ListViewItem item_y = (ListViewItem)y;

				//Get the sub-item values.
				if (m_columnNumber == 0)
				{
					string x_value = item_x.SubItems[m_columnNumber].Text;
					string y_value = item_y.SubItems[m_columnNumber].Text;
					if (m_order == SortOrder.Ascending)
						return string.Compare(x_value, y_value);
					else
						return string.Compare(y_value, x_value);
				}
				else
				{
					int x_value = int.Parse(item_x.SubItems[m_columnNumber].Text);
					int y_value = int.Parse(item_y.SubItems[m_columnNumber].Text);
					if (m_order == SortOrder.Ascending)
						return x_value - y_value;
					else
						return y_value - x_value;
				}
			}

			#endregion
		}

		private void btn_save_Click(object sender, EventArgs e)
		{
			SaveFileDialog opener = new SaveFileDialog();
			opener.RestoreDirectory = true;
			// Default file extension
			opener.DefaultExt = "xml";

			// Available file extensions
			opener.Filter = "Dictionary file (*.xml)|*.xml|CSV file (*.csv)|*.csv";

			// Adds a extension if the user does not
			opener.AddExtension = true;
			
			if (opener.ShowDialog(this) == DialogResult.OK)
			{
				StreamWriter writer = new StreamWriter(opener.FileName, false, Encoding.UTF8);

				StringBuilder sb = new StringBuilder();
				if (opener.FileName.EndsWith("xml", StringComparison.OrdinalIgnoreCase))
					FillStringBuilderWithXmlData(sb, lstVw_wordList.Items);
				else
					FillStringBuilderWithCsvData(sb, lstVw_wordList.Items);
				
				writer.Write(sb.ToString());
				writer.Flush();
				writer.Close();
				writer.Dispose();
			}
		}

		private static void FillStringBuilderWithXmlData(StringBuilder sb, ListView.ListViewItemCollection listViewItemCollection)
		{
			sb.AppendLine("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.AppendLine("<wordlist>");
			foreach (ListViewItem item in listViewItemCollection)
			{
				sb.AppendFormat("\t<w f=\"{1}\">{0}</w>", item.SubItems[0].Text.Trim(), item.SubItems[1].Text.Trim());
				sb.AppendLine();
			}
			sb.AppendLine("</wordlist>");
		}

		private static void FillStringBuilderWithCsvData(StringBuilder sb, ListView.ListViewItemCollection listViewItemCollection)
		{
			foreach (ListViewItem item in listViewItemCollection)
			{
				sb.AppendFormat("{0},{1}", item.SubItems[0].Text.Trim(), item.SubItems[1].Text.Trim());
				sb.AppendLine();
			}
		}

		private void btn_clearList_Click(object sender, EventArgs e)
		{
			ClearOutputControls();
			txtBx_textToParse.Text = "";
		}
	}
}

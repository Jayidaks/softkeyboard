namespace WordListCreator
{
	partial class MainForm
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.txtBx_textToParse = new System.Windows.Forms.TextBox();
			this.label1 = new System.Windows.Forms.Label();
			this.lstVw_wordList = new System.Windows.Forms.ListView();
			this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
			this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
			this.label2 = new System.Windows.Forms.Label();
			this.btn_parseText = new System.Windows.Forms.Button();
			this.btn_deleteWords = new System.Windows.Forms.Button();
			this.groupBox1 = new System.Windows.Forms.GroupBox();
			this.btn_filter = new System.Windows.Forms.Button();
			this.chkBx_keepTopWords = new System.Windows.Forms.CheckBox();
			this.num_keepTopWords = new System.Windows.Forms.NumericUpDown();
			this.chkBx_removeTheLast = new System.Windows.Forms.CheckBox();
			this.chkBx_removeWordsWithFrequencyLessThan = new System.Windows.Forms.CheckBox();
			this.chkBx_removeWordsShorterThan = new System.Windows.Forms.CheckBox();
			this.num_removeWordsShorterThan = new System.Windows.Forms.NumericUpDown();
			this.num_removeTheLast = new System.Windows.Forms.NumericUpDown();
			this.num_removeWordsWithFrequencyLessThan = new System.Windows.Forms.NumericUpDown();
			this.label3 = new System.Windows.Forms.Label();
			this.lbl_wordCount = new System.Windows.Forms.Label();
			this.lbl_bytesCount = new System.Windows.Forms.Label();
			this.label5 = new System.Windows.Forms.Label();
			this.btn_clearList = new System.Windows.Forms.Button();
			this.btn_save = new System.Windows.Forms.Button();
			this.lbl_parsingStatus = new System.Windows.Forms.Label();
			this.groupBox1.SuspendLayout();
			((System.ComponentModel.ISupportInitialize)(this.num_keepTopWords)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeWordsShorterThan)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeTheLast)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeWordsWithFrequencyLessThan)).BeginInit();
			this.SuspendLayout();
			// 
			// txtBx_textToParse
			// 
			this.txtBx_textToParse.Location = new System.Drawing.Point(12, 25);
			this.txtBx_textToParse.Multiline = true;
			this.txtBx_textToParse.Name = "txtBx_textToParse";
			this.txtBx_textToParse.Size = new System.Drawing.Size(698, 246);
			this.txtBx_textToParse.TabIndex = 0;
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(9, 9);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(266, 13);
			this.label1.TabIndex = 1;
			this.label1.Text = "Paste into the textbox below the text you wish to parse:";
			// 
			// lstVw_wordList
			// 
			this.lstVw_wordList.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2});
			this.lstVw_wordList.Location = new System.Drawing.Point(12, 334);
			this.lstVw_wordList.Name = "lstVw_wordList";
			this.lstVw_wordList.Size = new System.Drawing.Size(355, 285);
			this.lstVw_wordList.TabIndex = 2;
			this.lstVw_wordList.UseCompatibleStateImageBehavior = false;
			this.lstVw_wordList.View = System.Windows.Forms.View.Details;
			this.lstVw_wordList.ColumnClick += new System.Windows.Forms.ColumnClickEventHandler(this.lstVw_wordList_ColumnClick);
			// 
			// columnHeader1
			// 
			this.columnHeader1.Text = "Word";
			this.columnHeader1.Width = 265;
			// 
			// columnHeader2
			// 
			this.columnHeader2.Text = "Frequency";
			this.columnHeader2.Width = 86;
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(12, 318);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(83, 13);
			this.label2.TabIndex = 3;
			this.label2.Text = "Word list till now";
			// 
			// btn_parseText
			// 
			this.btn_parseText.Location = new System.Drawing.Point(12, 277);
			this.btn_parseText.Name = "btn_parseText";
			this.btn_parseText.Size = new System.Drawing.Size(75, 23);
			this.btn_parseText.TabIndex = 4;
			this.btn_parseText.Text = "&Parse";
			this.btn_parseText.UseVisualStyleBackColor = true;
			this.btn_parseText.Click += new System.EventHandler(this.btn_parseText_Click);
			// 
			// btn_deleteWords
			// 
			this.btn_deleteWords.Location = new System.Drawing.Point(373, 355);
			this.btn_deleteWords.Name = "btn_deleteWords";
			this.btn_deleteWords.Size = new System.Drawing.Size(142, 23);
			this.btn_deleteWords.TabIndex = 5;
			this.btn_deleteWords.Text = "&Delete selected words";
			this.btn_deleteWords.UseVisualStyleBackColor = true;
			// 
			// groupBox1
			// 
			this.groupBox1.Controls.Add(this.btn_filter);
			this.groupBox1.Controls.Add(this.chkBx_keepTopWords);
			this.groupBox1.Controls.Add(this.num_keepTopWords);
			this.groupBox1.Controls.Add(this.chkBx_removeTheLast);
			this.groupBox1.Controls.Add(this.chkBx_removeWordsWithFrequencyLessThan);
			this.groupBox1.Controls.Add(this.chkBx_removeWordsShorterThan);
			this.groupBox1.Controls.Add(this.num_removeWordsShorterThan);
			this.groupBox1.Controls.Add(this.num_removeTheLast);
			this.groupBox1.Controls.Add(this.num_removeWordsWithFrequencyLessThan);
			this.groupBox1.Location = new System.Drawing.Point(373, 444);
			this.groupBox1.Name = "groupBox1";
			this.groupBox1.Size = new System.Drawing.Size(336, 175);
			this.groupBox1.TabIndex = 6;
			this.groupBox1.TabStop = false;
			this.groupBox1.Text = "Filter";
			// 
			// btn_filter
			// 
			this.btn_filter.Location = new System.Drawing.Point(7, 146);
			this.btn_filter.Name = "btn_filter";
			this.btn_filter.Size = new System.Drawing.Size(75, 23);
			this.btn_filter.TabIndex = 11;
			this.btn_filter.Text = "&Filter";
			this.btn_filter.UseVisualStyleBackColor = true;
			this.btn_filter.Click += new System.EventHandler(this.btn_filter_Click);
			// 
			// chkBx_keepTopWords
			// 
			this.chkBx_keepTopWords.AutoSize = true;
			this.chkBx_keepTopWords.Location = new System.Drawing.Point(6, 97);
			this.chkBx_keepTopWords.Name = "chkBx_keepTopWords";
			this.chkBx_keepTopWords.Size = new System.Drawing.Size(88, 17);
			this.chkBx_keepTopWords.TabIndex = 10;
			this.chkBx_keepTopWords.Text = "Keep the first";
			this.chkBx_keepTopWords.UseVisualStyleBackColor = true;
			// 
			// num_keepTopWords
			// 
			this.num_keepTopWords.Location = new System.Drawing.Point(228, 96);
			this.num_keepTopWords.Name = "num_keepTopWords";
			this.num_keepTopWords.Size = new System.Drawing.Size(102, 20);
			this.num_keepTopWords.TabIndex = 9;
			// 
			// chkBx_removeTheLast
			// 
			this.chkBx_removeTheLast.AutoSize = true;
			this.chkBx_removeTheLast.Location = new System.Drawing.Point(6, 71);
			this.chkBx_removeTheLast.Name = "chkBx_removeTheLast";
			this.chkBx_removeTheLast.Size = new System.Drawing.Size(103, 17);
			this.chkBx_removeTheLast.TabIndex = 8;
			this.chkBx_removeTheLast.Text = "Remove the last";
			this.chkBx_removeTheLast.UseVisualStyleBackColor = true;
			// 
			// chkBx_removeWordsWithFrequencyLessThan
			// 
			this.chkBx_removeWordsWithFrequencyLessThan.AutoSize = true;
			this.chkBx_removeWordsWithFrequencyLessThan.Location = new System.Drawing.Point(6, 45);
			this.chkBx_removeWordsWithFrequencyLessThan.Name = "chkBx_removeWordsWithFrequencyLessThan";
			this.chkBx_removeWordsWithFrequencyLessThan.Size = new System.Drawing.Size(214, 17);
			this.chkBx_removeWordsWithFrequencyLessThan.TabIndex = 7;
			this.chkBx_removeWordsWithFrequencyLessThan.Text = "Remove words with frequency less than";
			this.chkBx_removeWordsWithFrequencyLessThan.UseVisualStyleBackColor = true;
			// 
			// chkBx_removeWordsShorterThan
			// 
			this.chkBx_removeWordsShorterThan.AutoSize = true;
			this.chkBx_removeWordsShorterThan.Location = new System.Drawing.Point(6, 19);
			this.chkBx_removeWordsShorterThan.Name = "chkBx_removeWordsShorterThan";
			this.chkBx_removeWordsShorterThan.Size = new System.Drawing.Size(156, 17);
			this.chkBx_removeWordsShorterThan.TabIndex = 6;
			this.chkBx_removeWordsShorterThan.Text = "Remove words shorter than";
			this.chkBx_removeWordsShorterThan.UseVisualStyleBackColor = true;
			// 
			// num_removeWordsShorterThan
			// 
			this.num_removeWordsShorterThan.Location = new System.Drawing.Point(228, 18);
			this.num_removeWordsShorterThan.Name = "num_removeWordsShorterThan";
			this.num_removeWordsShorterThan.Size = new System.Drawing.Size(102, 20);
			this.num_removeWordsShorterThan.TabIndex = 5;
			// 
			// num_removeTheLast
			// 
			this.num_removeTheLast.Location = new System.Drawing.Point(228, 70);
			this.num_removeTheLast.Name = "num_removeTheLast";
			this.num_removeTheLast.Size = new System.Drawing.Size(102, 20);
			this.num_removeTheLast.TabIndex = 4;
			// 
			// num_removeWordsWithFrequencyLessThan
			// 
			this.num_removeWordsWithFrequencyLessThan.Location = new System.Drawing.Point(228, 44);
			this.num_removeWordsWithFrequencyLessThan.Name = "num_removeWordsWithFrequencyLessThan";
			this.num_removeWordsWithFrequencyLessThan.Size = new System.Drawing.Size(102, 20);
			this.num_removeWordsWithFrequencyLessThan.TabIndex = 3;
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(374, 334);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(66, 13);
			this.label3.TabIndex = 7;
			this.label3.Text = "Word count:";
			// 
			// lbl_wordCount
			// 
			this.lbl_wordCount.AutoSize = true;
			this.lbl_wordCount.Location = new System.Drawing.Point(446, 334);
			this.lbl_wordCount.Name = "lbl_wordCount";
			this.lbl_wordCount.Size = new System.Drawing.Size(13, 13);
			this.lbl_wordCount.TabIndex = 8;
			this.lbl_wordCount.Text = "0";
			// 
			// lbl_bytesCount
			// 
			this.lbl_bytesCount.AutoSize = true;
			this.lbl_bytesCount.Location = new System.Drawing.Point(599, 334);
			this.lbl_bytesCount.Name = "lbl_bytesCount";
			this.lbl_bytesCount.Size = new System.Drawing.Size(13, 13);
			this.lbl_bytesCount.TabIndex = 10;
			this.lbl_bytesCount.Text = "0";
			// 
			// label5
			// 
			this.label5.AutoSize = true;
			this.label5.Location = new System.Drawing.Point(527, 334);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(66, 13);
			this.label5.TabIndex = 9;
			this.label5.Text = "Word count:";
			// 
			// btn_clearList
			// 
			this.btn_clearList.Location = new System.Drawing.Point(373, 384);
			this.btn_clearList.Name = "btn_clearList";
			this.btn_clearList.Size = new System.Drawing.Size(142, 23);
			this.btn_clearList.TabIndex = 11;
			this.btn_clearList.Text = "&Clear";
			this.btn_clearList.UseVisualStyleBackColor = true;
			this.btn_clearList.Click += new System.EventHandler(this.btn_clearList_Click);
			// 
			// btn_save
			// 
			this.btn_save.Location = new System.Drawing.Point(373, 413);
			this.btn_save.Name = "btn_save";
			this.btn_save.Size = new System.Drawing.Size(142, 23);
			this.btn_save.TabIndex = 12;
			this.btn_save.Text = "&Save";
			this.btn_save.UseVisualStyleBackColor = true;
			this.btn_save.Click += new System.EventHandler(this.btn_save_Click);
			// 
			// lbl_parsingStatus
			// 
			this.lbl_parsingStatus.AutoSize = true;
			this.lbl_parsingStatus.Location = new System.Drawing.Point(93, 282);
			this.lbl_parsingStatus.Name = "lbl_parsingStatus";
			this.lbl_parsingStatus.Size = new System.Drawing.Size(0, 13);
			this.lbl_parsingStatus.TabIndex = 13;
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(721, 671);
			this.Controls.Add(this.lbl_parsingStatus);
			this.Controls.Add(this.btn_save);
			this.Controls.Add(this.btn_clearList);
			this.Controls.Add(this.lbl_bytesCount);
			this.Controls.Add(this.label5);
			this.Controls.Add(this.lbl_wordCount);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.groupBox1);
			this.Controls.Add(this.btn_deleteWords);
			this.Controls.Add(this.btn_parseText);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.lstVw_wordList);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.txtBx_textToParse);
			this.Name = "MainForm";
			this.Text = "Word list creator";
			this.groupBox1.ResumeLayout(false);
			this.groupBox1.PerformLayout();
			((System.ComponentModel.ISupportInitialize)(this.num_keepTopWords)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeWordsShorterThan)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeTheLast)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.num_removeWordsWithFrequencyLessThan)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.TextBox txtBx_textToParse;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.ListView lstVw_wordList;
		private System.Windows.Forms.ColumnHeader columnHeader1;
		private System.Windows.Forms.ColumnHeader columnHeader2;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Button btn_parseText;
		private System.Windows.Forms.Button btn_deleteWords;
		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.CheckBox chkBx_removeTheLast;
		private System.Windows.Forms.CheckBox chkBx_removeWordsWithFrequencyLessThan;
		private System.Windows.Forms.CheckBox chkBx_removeWordsShorterThan;
		private System.Windows.Forms.NumericUpDown num_removeWordsShorterThan;
		private System.Windows.Forms.NumericUpDown num_removeTheLast;
		private System.Windows.Forms.NumericUpDown num_removeWordsWithFrequencyLessThan;
		private System.Windows.Forms.CheckBox chkBx_keepTopWords;
		private System.Windows.Forms.NumericUpDown num_keepTopWords;
		private System.Windows.Forms.Button btn_filter;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label lbl_wordCount;
		private System.Windows.Forms.Label lbl_bytesCount;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.Button btn_clearList;
		private System.Windows.Forms.Button btn_save;
		private System.Windows.Forms.Label lbl_parsingStatus;
	}
}


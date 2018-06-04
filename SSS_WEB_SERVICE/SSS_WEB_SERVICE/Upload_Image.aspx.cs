using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_SERVICE
{
	public partial class Upload_Image : System.Web.UI.Page
	{
		private int selecteValue = 0;
		protected void Page_Load(object sender, EventArgs e)
		{

			SSS_SERVICE service = new SSS_SERVICE();

			List<User> users = service.return_Users();





			int selected_Value = DropDownList1.SelectedIndex;
			if(!IsPostBack)
			{
				
				DropDownList1.DataSource = users;
				DropDownList1.DataTextField = "User_Name";
				DropDownList1.DataValueField = "Id";

				DropDownList1.DataBind();
				selecteValue = DropDownList1.SelectedIndex;
			}
			if (IsPostBack)
			{
				// the code that only needs to run once goes here
				DropDownList1.SelectedIndex = selected_Value;
				DropDownList1.DataSource = users;
				DropDownList1.DataTextField = "User_Name";
				DropDownList1.DataValueField = "Id";

				DropDownList1.DataBind();
			}
		}
		protected void Button1_Click(object sender, EventArgs e)
		{
			Label1.Text = "Retraining the model......";
			string image_Path = FileUpload1.FileName;
			byte[] file = ReadStream(FileUpload1.PostedFile.InputStream);
			string path = Server.MapPath("~/MATLAB_TRAIN_DATA/"+ DropDownList1.SelectedItem+ "/"+ image_Path);
			File.WriteAllBytes(@path, file);


			SSS_SERVICE service = new SSS_SERVICE();
			string selectedValue = DropDownList1.SelectedValue;

			service.INSERT_IMAGE(Convert.ToInt32(selectedValue), image_Path);
			Label2.Text = "Image uploaded successfully....";
			service.UPDATE_TRAIN_DATA(Convert.ToInt32(selectedValue), path);
			Label3.Text = "Training data updated successfully.....";
			Label4.Text = "Training finished with Accuracy = " + Regex.Split(service.TRAIN_IMAGES_MODEL(Convert.ToInt32(selectedValue)), "=")[1] + "%";

		}


		public static byte[] ReadStream(Stream input)
		{
			byte[] buffer = new byte[16 * 1024];
			using (MemoryStream ms = new MemoryStream())
			{
				int read;
				while ((read = input.Read(buffer, 0, buffer.Length)) > 0)
				{
					ms.Write(buffer, 0, read);
				}
				return ms.ToArray();
			}
		}

		
	}
}
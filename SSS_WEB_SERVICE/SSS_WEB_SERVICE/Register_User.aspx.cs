using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_SERVICE
{
	public partial class Register_User : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{

		}

		protected void Button1_Click(object sender, EventArgs e)
		{
			SSS_SERVICE service = new SSS_SERVICE();
			Label1.Text = service.INSERT_USER(TextBox1.Text);
			System.IO.Directory.CreateDirectory(Server.MapPath("MATLAB_TRAIN_DATA/" + TextBox1.Text));
		}
	}
}
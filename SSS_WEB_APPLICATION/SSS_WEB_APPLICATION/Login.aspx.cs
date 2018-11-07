using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using SSS_WEB_APPLICATION.SSS_SERVICE;

namespace SSS_WEB_APPLICATION
{
	public partial class Login : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{
			
		}

		protected void sign_in_Click(object sender, EventArgs e)
		{
			if(!username.Text.Equals(""))
			{
				if (!password.Text.Equals(""))
				{
					SSS_SERVICE_LOCAL service = new SSS_SERVICE_LOCAL();
					User user = service.LOGIN(username.Text, password.Text);
					if (user != null)
					{
						Session["user"] = user;
						Response.Redirect("DefaultClient.aspx?aefsregsfdssssdtadsryhgnju=" + user.Id);
					}
					else
					{
						result.Text = "Wrong username or password!";
					}
				}
				else
				{
					result.Text = "Password Textbox empty";
				}
			}
			else
			{
				result.Text = "Username Textbox empty";
			}

		}
	}
}
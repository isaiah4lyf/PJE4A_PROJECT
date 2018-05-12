using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.IO;

namespace SSS_WEB_SERVICE
{
	public partial class Index : System.Web.UI.Page
	{
		public MLApp.MLApp matlab;
		protected void Page_Load(object sender, EventArgs e)
		{
			matlab = new MLApp.MLApp();
		}

		protected void Button1_Click(object sender, EventArgs e)
		{
			String[] Data = new String[] { "isaiah", "Cool" };
			matlab.PutWorkspaceData("varName", "base", Data);

			//matlab.Execute("run('C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_APPLICATION/SSS_WEB_APPLICATION/MATLAB_SCRIPTS/RUN_ESS.m')");
			//Label1.Text = matlab.Execute("run('C:/Users/isaia/Downloads/201400517/Load_Data_and_Run_Algorithm.m')");
			//Label1.Text = matlab.Execute("varName[1]");
			//string  response = matlab.Execute("Gog_F") +"\n";
			//response += matlab.Execute("Cow_F") + "\n";
			//Label1.Text = response;


			string image_Path = Server.MapPath(FileUpload1.FileName);
			SSS_SERVICE service = new SSS_SERVICE();

			Label1.Text = service.INSERT_IMAGE(0, image_Path);
		}
	}
}
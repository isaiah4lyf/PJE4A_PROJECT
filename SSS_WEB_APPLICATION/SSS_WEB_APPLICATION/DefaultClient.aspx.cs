using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using SSS_WEB_APPLICATION.SSS_SERVICE;

namespace SSS_WEB_APPLICATION
{
	public partial class DefaultClient : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{
			User user = (User)Session["user"];
			Device_Tracker.NavigateUrl = "TrackerClient.aspx?aefsregsfdssssdtadsryhgnju=" + user.Id;
			Training_Images.NavigateUrl = "TrainingImages.aspx?aefsregsfdssssdtadsryhgnju=" + user.Id;
			Prediction_Images.NavigateUrl = "PredictionImages.aspx?aefsregsfdssssdtadsryhgnju=" + user.Id;
			Upload_News_Feed.NavigateUrl = "UploadNewsFeed.aspx";
		}
	}
}
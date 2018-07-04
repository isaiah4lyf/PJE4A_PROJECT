﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_SERVICE
{
	public partial class View_Users : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{
			SSS_SERVICE service = new SSS_SERVICE();
			List<User> users = service.return_Users();
			List<Image> images = service.return_Images();
			string temp = "";
			string path = "MATLAB_TRAIN_DATA/";
			for (int i = 0; i < users.Count; i++)
			{
				temp += "<br/><h3>" + users.ElementAt(i).User_Name + "</h3><br/>";
				temp += "<div class='row'>";
				
				for (int j = 0; j < images.Count; j++)
				{
					if(images.ElementAt(j).User_ID == users.ElementAt(i).Id)
					{
						temp += "<div class='col-lg-3 col-md-6'>";
						temp += "<div class='single-service'>";
						temp += "<img style='border-radius:25px'  class='img-fluid' src='" + path + users.ElementAt(i).User_Name + "/"+ images.ElementAt(j).Image_Path+"' alt=''>";
						temp += "</div>";
						temp += "</div>";
					}

				}
				temp += "</div>";
			}

			div.InnerHtml = temp;

		}
		protected void Button1_Click(object sender, EventArgs e)
		{

		}
	}
}
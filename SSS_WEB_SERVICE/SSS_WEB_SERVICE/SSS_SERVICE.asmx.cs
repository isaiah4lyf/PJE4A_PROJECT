using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Services;

namespace SSS_WEB_SERVICE
{
	/// <summary>
	/// Summary description for SSS_SERVICE
	/// </summary>
	[WebService(Namespace = "http://tempuri.org/")]
	[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
	[System.ComponentModel.ToolboxItem(false)]
	// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
	// [System.Web.Script.Services.ScriptService]
	public class SSS_SERVICE : System.Web.Services.WebService
	{

		public string Matlab_Path = "C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/";
		public MLApp.MLApp matlab = new MLApp.MLApp();
		public SSS_LINQ_DataContext linq = new SSS_LINQ_DataContext();



		[WebMethod]
		public string INSERT_IMAGE(int user_ID, string image_Path)
		{

			Image image_Table = new Image();
			image_Table.User_ID = user_ID;
			image_Table.Image_Path = image_Path;
			linq.Images.InsertOnSubmit(image_Table);
			linq.SubmitChanges();

			return "true";

		}

		[WebMethod]
		public string INSERT_USER(string user_Name)
		{
			List<Trained_Model> table = (from Trained_Model in linq.Trained_Models						
						 select Trained_Model).ToList();
			string status = "false";
			if(table.Count == 0)
			{
				Trained_Model model = new Trained_Model();
				/// Model_'model number'_'Model version'
				model.Model_Name = "Model_01";
				model.Number_OF_Users = 1;
				model.Model_Version = 1;
				linq.Trained_Models.InsertOnSubmit(model);
				linq.SubmitChanges();

				List<Trained_Model> updated_Models = (from Trained_Model in linq.Trained_Models
											 select Trained_Model).ToList();
				User user = new User();
				user.User_Name = user_Name;
				user.Model_ID = updated_Models.ElementAt(0).Id;
				linq.Users.InsertOnSubmit(user);
				linq.SubmitChanges();
				status = "true";
			}
			else
			{
				if(table.ElementAt(table.Count - 1).Number_OF_Users < 4)
				{
					Trained_Model row_To_Update = (from Trained_Model in linq.Trained_Models
												   where Trained_Model.Id == table.ElementAt(table.Count - 1).Id
												   select Trained_Model).First();
					row_To_Update.Number_OF_Users = row_To_Update.Number_OF_Users + 1;
					linq.SubmitChanges();

					User user = new User();
					user.User_Name = user_Name;
					user.Model_ID = table.ElementAt(table.Count - 1).Id;
					linq.Users.InsertOnSubmit(user);
					linq.SubmitChanges();
					status = "true";
				}
				else
				{
					Trained_Model model = new Trained_Model();
					int model_num = table.Count + 1;
					model.Model_Name = "Model_0"+ model_num;
					model.Number_OF_Users = 1;
					model.Model_Version = 1;
					linq.Trained_Models.InsertOnSubmit(model);
					linq.SubmitChanges();


					List<Trained_Model> updated_Models = (from Trained_Model in linq.Trained_Models
														  select Trained_Model).ToList();
					User user = new User();
					user.User_Name = user_Name;
					user.Model_ID = updated_Models.ElementAt(updated_Models.Count - 1).Id;
					linq.Users.InsertOnSubmit(user);
					linq.SubmitChanges();
					status = "true";
				}
			}
			return status;
		}

		//remember to change image name to be an actual image using binary
		[WebMethod]
		public string UPDATE_TRAIN_DATA(int user_ID,string image_Path)
		{
			string return_status = "false";
			User user = (from User in linq.Users
						 where Convert.ToInt32(User.Id) == user_ID
						 select User).First();

			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version + 1);
			string new_model_Name = model_To_Update.Model_Name + "_" + new_Model_Verion;



			matlab.PutWorkspaceData("image_Path", "base", image_Path);
			matlab.PutWorkspaceData("user_ID", "base", user_ID);



			string ML_features_Database = Matlab_Path + "MATLAB_TRAIN_DATA/" + model_To_Update.Model_Name + "_" + model_To_Update.Model_Version + ".mat";
			string ML_features_Database2 = Matlab_Path + "MATLAB_TRAIN_DATA/" + new_model_Name + ".mat";
			matlab.PutWorkspaceData("path", "base", ML_features_Database);
			matlab.PutWorkspaceData("path2", "base", ML_features_Database2);


			return_status = matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/Update_Training_Data11.m')");

			
			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return user_ID.ToString();
		}

		[WebMethod]
		public string UPDATE_TRAIN_DATA2(int user_ID)
		{
			string return_status = "false";
			User user = (from User in linq.Users
								   where Convert.ToInt32(User.Id) == user_ID
								   select User).First();

			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version + 1);
			string new_model_Name = model_To_Update.Model_Name + "_" + new_Model_Verion;

			List<User> users = (from User in linq.Users
								where User.Model_ID == model_To_Update.Id
								select User).ToList();
			for(int i = 0; i < model_To_Update.Number_OF_Users; i++)
			{
				List<Image> images = (from Image in linq.Images
									  where Image.User_ID == users.ElementAt(i).Id
									  select Image).ToList();
				for (int j = 0; j < images.Count; j++)
				{
					matlab.PutWorkspaceData("image_Path", "base", images.ElementAt(j).Image_Path);
					matlab.PutWorkspaceData("user_ID", "base", user_ID);



					string ML_features_Database = Matlab_Path + "MATLAB_TRAIN_DATA/" + model_To_Update.Model_Name + "_" + model_To_Update.Model_Version + ".mat";
					string ML_features_Database2 = Matlab_Path + "MATLAB_TRAIN_DATA/" + new_model_Name + ".mat";
					matlab.PutWorkspaceData("path", "base", ML_features_Database);
					matlab.PutWorkspaceData("path2", "base", ML_features_Database2);


					return_status = matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/Update_Training_Data11.m')");
					
				}
			}
			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return return_status;
		}

		[WebMethod]
		public string TRAIN_IMAGES_MODEL(int user_ID)
		{
			User user = (from User in linq.Users
						 where User.Id == user_ID
						 select User).First();
			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			List<User> users = (from User in linq.Users
								where User.Model_ID == model_To_Update.Id
								select User).ToList();

			if(users.Count == 4)
			{
				matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
				matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
				matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
				matlab.PutWorkspaceData("class_4", "base", users.ElementAt(3).Id);
			}
			else if(users.Count == 3)
			{
				matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
				matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
				matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
				matlab.PutWorkspaceData("class_4", "base", users.ElementAt(2).Id + 1);
			}
			else if(users.Count == 2)
			{
				matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
				matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
				matlab.PutWorkspaceData("class_3", "base", users.ElementAt(1).Id + 1);
				matlab.PutWorkspaceData("class_4", "base", users.ElementAt(1).Id + 2);
			}
			else
			{
				matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
				matlab.PutWorkspaceData("class_2", "base", users.ElementAt(0).Id + 2);
				matlab.PutWorkspaceData("class_3", "base", users.ElementAt(0).Id + 3);
				matlab.PutWorkspaceData("class_4", "base", users.ElementAt(0).Id + 4);
			}

			string ML_features_Database = Matlab_Path+ "MATLAB_TRAIN_DATA/" + model_To_Update.Model_Name + "_" + model_To_Update.Model_Version + ".mat";
			matlab.PutWorkspaceData("path", "base", ML_features_Database);

			string Trained_Model_File = Matlab_Path + "MATLAB_TRAINED_MODELS/" + model_To_Update.Model_Name + "_" + model_To_Update.Model_Version + ".mat";
			matlab.PutWorkspaceData("path2", "base", Trained_Model_File);
			return matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/RUN_ESS5.m')");
		}

		[WebMethod]
		public string PREDICT_USER(string image_Name)
		{
			List<Trained_Model> trained_Models = (from Trained_Model in linq.Trained_Models
												  select Trained_Model).ToList();
			int[] results = new int[trained_Models.Count];
			int[] fet_Match = new int[trained_Models.Count];
			for(int i = 0; i < trained_Models.Count; i++)
			{
				List<User> users = (from User in linq.Users
									where User.Model_ID == trained_Models.ElementAt(i).Id
									select User).ToList();
				matlab.Execute("'clear all'");

				if (users.Count == 4)
				{
					matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
					matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
					matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
					matlab.PutWorkspaceData("class_4", "base", users.ElementAt(3).Id);
				}
				else if (users.Count == 3)
				{
					matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
					matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
					matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
					matlab.PutWorkspaceData("class_4", "base", users.ElementAt(2).Id + 1);
				}
				else if (users.Count == 2)
				{
					matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
					matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
					matlab.PutWorkspaceData("class_3", "base", users.ElementAt(1).Id + 1);
					matlab.PutWorkspaceData("class_4", "base", users.ElementAt(1).Id + 2);
				}
				else
				{
					matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
					matlab.PutWorkspaceData("class_2", "base", users.ElementAt(0).Id + 2);
					matlab.PutWorkspaceData("class_3", "base", users.ElementAt(0).Id + 3);
					matlab.PutWorkspaceData("class_4", "base", users.ElementAt(0).Id + 4);
				}


				matlab.PutWorkspaceData("Image_Name_of", "base", image_Name);

				string Trained_Model2 = Matlab_Path + "MATLAB_TRAINED_MODELS/" + trained_Models.ElementAt(i).Model_Name + "_" +trained_Models.ElementAt(i).Model_Version + ".mat";
				matlab.PutWorkspaceData("path", "base", Trained_Model2);
				matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/Predict_User10.m')");
				results[i] = Convert.ToInt32(Regex.Split(matlab.Execute("max_Class").Replace(" ", string.Empty), "=")[1]); 
				string[] value0 = Regex.Split(matlab.Execute("max_Num").Replace(" ", string.Empty),"=");
				fet_Match[i] = Convert.ToInt32(value0[1]);
			}
			int max = fet_Match.Max();
			string result_With_Max = "";
			for(int i = 0; i < trained_Models.Count; i++)
			{
				if(fet_Match[i] == max)
				{
					User user = (from User in linq.Users
										where User.Id == results[i]
								 select User).First();
					result_With_Max = user.User_Name;
				}
			}
			return result_With_Max;
		}

		[WebMethod]
		public List<User> return_Users()
		{
			List<User> users = (from User in linq.Users
								select User).ToList();
			return users;
		}

		[WebMethod]
		public List<Image> return_Images()
		{
			List<Image> images = (from Image in linq.Images
								select Image).ToList();
			return images;
		}
	}
}

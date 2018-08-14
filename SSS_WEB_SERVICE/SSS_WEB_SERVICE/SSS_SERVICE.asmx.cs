using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Services;
using MLApp;
using MathWorks.MATLAB.NET.Arrays;
using MathWorks.MATLAB.NET.Utility;
using myIntegrand;
using System;
using System.Net;
using System.Net.Sockets;

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

		public SSS_LINQ_DataContext linq = new SSS_LINQ_DataContext();

		//GENERAL METHODS
		[WebMethod]
		public string INSERT_USER(string user_Name,string password,string email)
		{
			string status = "false";
			List<User> userTable = (from User in linq.Users
									where User.User_Name == user_Name
									select User).ToList();

			if(userTable.Count == 0)
			{
				List<Trained_Model> table = (from Trained_Model in linq.Trained_Models
											 select Trained_Model).ToList();
				List<Trained_Models_Voice_Note> table_VN = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
															select Trained_Models_Voice_Note).ToList();

				if (table.Count == 0)
				{
					Trained_Model model = new Trained_Model();
					/// Model_'model number'_'Model version'
					model.Model_Name = "Model_01";
					model.Number_OF_Users = 1;
					model.Model_Version = 1;
					linq.Trained_Models.InsertOnSubmit(model);
					linq.SubmitChanges();

					Trained_Models_Voice_Note VN_model = new Trained_Models_Voice_Note();
					VN_model.Model_Name = "Model_01";
					VN_model.Number_OF_Users = 1;
					VN_model.Model_Version = 1;
					linq.Trained_Models_Voice_Notes.InsertOnSubmit(VN_model);
					linq.SubmitChanges();

					List<Trained_Model> updated_Models = (from Trained_Model in linq.Trained_Models
														  select Trained_Model).ToList();

					List<Trained_Models_Voice_Note> updated_Models_vn = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
																		 select Trained_Models_Voice_Note).ToList();
					User user = new User();
					user.User_Name = user_Name;
					user.Password = password;
					user.Email = email;
					user.Model_ID = updated_Models.ElementAt(0).Id;
					user.Model_ID_VN = updated_Models_vn.ElementAt(0).Id;
					linq.Users.InsertOnSubmit(user);
					linq.SubmitChanges();


					status = "true";
				}
				else
				{
					if (table.ElementAt(table.Count - 1).Number_OF_Users < 4)
					{
						Trained_Model row_To_Update = (from Trained_Model in linq.Trained_Models
													   where Trained_Model.Id == table.ElementAt(table.Count - 1).Id
													   select Trained_Model).First();
						row_To_Update.Number_OF_Users = row_To_Update.Number_OF_Users + 1;
						linq.SubmitChanges();

						Trained_Models_Voice_Note model_to_Update_VN = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
																		where Trained_Models_Voice_Note.Id == table_VN.ElementAt(table_VN.Count - 1).Id
																		select Trained_Models_Voice_Note).First();
						model_to_Update_VN.Number_OF_Users = model_to_Update_VN.Number_OF_Users + 1;
						linq.SubmitChanges();

						User user = new User();
						user.User_Name = user_Name;
						user.Password = password;
						user.Email = email;
						user.Model_ID = table.ElementAt(table.Count - 1).Id;
						user.Model_ID_VN = table_VN.ElementAt(table_VN.Count - 1).Id;
						linq.Users.InsertOnSubmit(user);
						linq.SubmitChanges();
						status = "true";
					}
					else
					{
						Trained_Model model = new Trained_Model();
						int model_num = table.Count + 1;
						model.Model_Name = "Model_0" + model_num;
						model.Number_OF_Users = 1;
						model.Model_Version = 1;
						linq.Trained_Models.InsertOnSubmit(model);
						linq.SubmitChanges();

						Trained_Models_Voice_Note model_vn = new Trained_Models_Voice_Note();

						model_vn.Model_Name = "Model_0" + model_num;
						model_vn.Number_OF_Users = 1;
						model_vn.Model_Version = 1;
						linq.Trained_Models_Voice_Notes.InsertOnSubmit(model_vn);
						linq.SubmitChanges();


						List<Trained_Model> updated_Models = (from Trained_Model in linq.Trained_Models
															  select Trained_Model).ToList();
						List<Trained_Models_Voice_Note> updated_Models_vn = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
																			 select Trained_Models_Voice_Note).ToList();
						User user = new User();
						user.User_Name = user_Name;
						user.Password = password;
						user.Email = email;
						user.Model_ID = updated_Models.ElementAt(updated_Models.Count - 1).Id;
						user.Model_ID_VN = updated_Models_vn.ElementAt(updated_Models_vn.Count - 1).Id;
						linq.Users.InsertOnSubmit(user);
						linq.SubmitChanges();
						status = "true";
					}
				}
			}

			return status;
		}

		//IMAGE PROCESSING METHODS
		[WebMethod]
		public string INSERT_IMAGE(string user_ID, string image_Path)
		{

			Image image_Table = new Image();
			image_Table.User_ID = Convert.ToUInt16(user_ID);
			image_Table.Image_Path = image_Path;
			linq.Images.InsertOnSubmit(image_Table);
			linq.SubmitChanges();

			return "true";

		}


		[WebMethod]
		public string UPDATE_TRAIN_DATA(String user_ID)
		{

			User user = (from User in linq.Users
						 where Convert.ToInt32(User.Id) == Convert.ToInt32(user_ID)
						 select User).First();

			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version + 1);
			string new_model_Name = model_To_Update.Model_Name + "_" + new_Model_Verion;


			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return new_model_Name;
		}


		[WebMethod]
		public string TRAIN_IMAGES_MODEL(string user_ID)
		{

			User user = (from User in linq.Users
						 where User.Id == Convert.ToInt16(user_ID)
						 select User).First();
			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			List<User> users = (from User in linq.Users
								where User.Model_ID == model_To_Update.Id
								select User).ToList();

			string users_ID = model_To_Update.Model_Name + "_" + model_To_Update.Model_Version;
			for(int i = 0; i < users.Count; i++)
			{
				users_ID += "," + users.ElementAt(i).Id;
			}
			return users_ID;
		}
		[WebMethod]
		public string[] RETURN_TRAINED_MODELS()
		{
			List<Trained_Model> trained_Models = (from Trained_Model in linq.Trained_Models
												  select Trained_Model).ToList();
			string[] trained_models = new string[trained_Models.Count];
			for(int i = 0; i < trained_Models.Count; i++)
			{
				trained_models[i] = trained_Models.ElementAt(i).Id + "," + trained_Models.ElementAt(i).Model_Name + "," + trained_Models.ElementAt(i).Number_OF_Users + "," + trained_Models.ElementAt(i).Model_Version;
			}
			return trained_models;
		}

		[WebMethod]
		public string[] RETURN_USERS_IN_MODEL(string Model_ID)
		{
			List<User> users = (from User in linq.Users
								where User.Model_ID == Convert.ToInt16(Model_ID)
								select User).ToList();
			string[] users_string = new string[users.Count];
			for(int i = 0; i < users.Count; i++ )
			{
				users_string[i] = users.ElementAt(i).Id + "," + users.ElementAt(i).User_Name + "," + users.ElementAt(i).Model_ID;
			}
			return users_string;
		}

		[WebMethod]
		public string RETURN_USER_WITH_ID(string user_ID)
		{
			User user = (from User in linq.Users
						 where User.Id == Convert.ToInt16(user_ID)
						 select User).First();
			string user_string = user.Id + "," + user.User_Name + "," + user.Model_ID;
			return user_string;
		}

		[WebMethod]
		public string DECREMENT_IMAGES_MODEL_VERSION(string user_ID)
		{
			User user = (from User in linq.Users
						 where Convert.ToInt32(User.Id) == Convert.ToInt32(user_ID)
						 select User).First();
			Trained_Model model_To_Update = (from Trained_Model in linq.Trained_Models
											 where Trained_Model.Id == user.Model_ID
											 select Trained_Model).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version - 1);

			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return "ok";
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

		[WebMethod]
		public string[] RETURN_IMAGES_FOR_MOBILE()
		{
			List<Image> images = (from Image in linq.Images
								  select Image).ToList();
			string[] images_arr = new string[images.Count];
			for(int i = 0; i < images.Count; i++)
			{
				images_arr[i] = images.ElementAt(i).Id + "," + images.ElementAt(i).Image_Path + "," + images.ElementAt(i).User_ID;
			}
			return images_arr;
		}


		//SOUND PROCESSING METHODS
		[WebMethod]
		public string INSERT_VOICE_NOTE(string user_ID, string voice_Note_Path)
		{

			Voice_Note VN_Table = new Voice_Note();
			VN_Table.User_ID = Convert.ToUInt16(user_ID);
			VN_Table.Voice_Note_Path = voice_Note_Path;
			linq.Voice_Notes.InsertOnSubmit(VN_Table);
			linq.SubmitChanges();
			return "true";

		}

		[WebMethod]
		public string UPDATE_TRAIN_DATA_VN(string user_ID)
		{

			User user = (from User in linq.Users
						 where Convert.ToInt32(User.Id) == Convert.ToInt32(user_ID)
						 select User).First();

			Trained_Models_Voice_Note model_To_Update = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
														 where Trained_Models_Voice_Note.Id == user.Model_ID_VN
											 select Trained_Models_Voice_Note).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version + 1);
			string new_model_Name = model_To_Update.Model_Name + "_" + new_Model_Verion;


			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return new_model_Name;
		}

		[WebMethod]
		public string DECREMENT_IMAGES_MODEL_VERSION_VN(string user_ID)
		{
			User user = (from User in linq.Users
						 where Convert.ToInt32(User.Id) == Convert.ToInt32(user_ID)
						 select User).First();
			Trained_Models_Voice_Note model_To_Update = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
														 where Trained_Models_Voice_Note.Id == user.Model_ID_VN
											 select Trained_Models_Voice_Note).First();

			int new_Model_Verion = Convert.ToInt32(model_To_Update.Model_Version - 1);

			model_To_Update.Model_Version = new_Model_Verion;
			linq.SubmitChanges();
			return "ok";
		}

		[WebMethod]
		public string[] RETURN_TRAINED_MODELS_VN()
		{
			List<Trained_Models_Voice_Note> trained_Models = (from Trained_Models_Voice_Note in linq.Trained_Models_Voice_Notes
															  select Trained_Models_Voice_Note).ToList();
			string[] trained_models = new string[trained_Models.Count];
			for (int i = 0; i < trained_Models.Count; i++)
			{
				trained_models[i] = trained_Models.ElementAt(i).Id + "," + trained_Models.ElementAt(i).Model_Name + "," + trained_Models.ElementAt(i).Number_OF_Users + "," + trained_Models.ElementAt(i).Model_Version;
			}
			return trained_models;
		}

		[WebMethod]
		public string[] RETURN_USERS_IN_MODEL_VN(string Model_ID)
		{
			List<User> users = (from User in linq.Users
								where User.Model_ID_VN == Convert.ToInt16(Model_ID)
								select User).ToList();
			string[] users_string = new string[users.Count];
			for (int i = 0; i < users.Count; i++)
			{
				users_string[i] = users.ElementAt(i).Id + "," + users.ElementAt(i).User_Name + "," + users.ElementAt(i).Model_ID_VN;
			}
			return users_string;
		}


	}

}

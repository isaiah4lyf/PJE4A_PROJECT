using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Script.Serialization;
using System.Web.Script.Services;
using System.Web.Services;
using Twilio;
using Twilio.Rest.Api.V2010.Account;
using Twilio.Types;


namespace SSS_WEB_SERVICE
{
    /// <summary>
    /// Summary description for SSS_SERVICE
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    [System.Web.Script.Services.ScriptService]
    public class SSS_SERVICE : System.Web.Services.WebService
    {

        public SSS_LINQ_DataContext linq = new SSS_LINQ_DataContext();

        //GENERAL METHODS
        [WebMethod]
        public string INSERT_DEVICE_COORDINATE(string Device_Mac, string Longitude, string Latitude)
        {
            Device_Coordinate coordinate = new Device_Coordinate();
            coordinate.Device_Mac = Device_Mac;
            coordinate.Longitude = Longitude;
            coordinate.Latitude = Latitude;
            coordinate.Time_At_This_Coordite = DateTime.Now.ToString("HH:mm:ss");
            coordinate.Date_At_This_Coordinate = DateTime.Today.ToString("dd-MM-yyyy");
            linq.Device_Coordinates.InsertOnSubmit(coordinate);
            linq.SubmitChanges();
            return "true";
        }

        [WebMethod]
        public List<Device_Coordinate> RETURN_DEVICE_COORDINATE_JS(string Device_Mac)
        {
            List<object> cord_ob_list = new List<object>();

            List<Device_Coordinate> coordinates = (from Device_Coordinate in linq.Device_Coordinates
                                                   where Device_Coordinate.Device_Mac == Device_Mac
                                                   select Device_Coordinate).ToList();
            return coordinates;
        }

        [WebMethod]
        public string CHECK_USERNAME(string user_Name)
        {
            string status = "false";
            List<User> userTable = (from User in linq.Users
                                    where User.User_Name == user_Name
                                    select User).ToList();
            if (userTable.Count > 0)
            {
                status = "true";
            }
            return status;
        }
        [WebMethod]
        public string SEND_SMS(string body, string From_num, string to_num)
        {
            var accountSid = "ACc484302f5efe76f989ed842c373f8102";
            var authToken = "03fc1567dca19d297dbb2fcd3107b36c";
            TwilioClient.Init(accountSid, authToken);

            var messageOptions = new CreateMessageOptions(
                new PhoneNumber(to_num));
            messageOptions.From = new PhoneNumber(From_num);
            messageOptions.Body = body;

            var message = MessageResource.Create(messageOptions);
            return message.Body;
        }
        [WebMethod]
        public string CHECK_USER_NAME_VISION(string User_Name)
        {
            List<User> user = (from User in linq.Users
                               where User.User_Name == User_Name
                               select User).ToList();
            if (user.Count > 0)
            {
                return user.ElementAt(0).Id + "," + user.ElementAt(0).User_Name;
            }
            else
            {
                return "false";
            }
        }
        [WebMethod]
        public void UPLOAD_NEWS_FEED_VIDEO(string title, string description, string readMoreLink, byte[] titleImageContents, string titleImageName, byte[] VideoContents, string VideoFilename)
        {
            News_Feed feed = new News_Feed();
            feed.Title = title;
            feed.Description = description;
            feed.ReadMoreLink = readMoreLink;
            feed.TitleImage = titleImageName;
            feed.Video = VideoFilename;
            feed.Image = "false";
            feed.UploadTime = DateTime.Now.ToString("HH:mm:ss");
            feed.UploadDate = DateTime.Today.ToString("dd-MM-yyyy");
            linq.News_Feeds.InsertOnSubmit(feed);
            linq.SubmitChanges();

            var videos = Server.MapPath("~/Videos");
            var file = Path.Combine(videos, Path.GetFileName(VideoFilename));
            File.WriteAllBytes(file, VideoContents);

            var file2 = Path.Combine(videos, Path.GetFileName(titleImageName));
            File.WriteAllBytes(file2, titleImageContents);
        }
        [WebMethod]
        public void UPLOAD_NEWS_FEED_IMAGE(string title, string description, string readMoreLink, byte[] titleImageContents, string titleImageName, byte[] ImageContents, string ImageFilename)
        {
            News_Feed feed = new News_Feed();
            feed.Title = title;
            feed.Description = description;
            feed.ReadMoreLink = readMoreLink;
            feed.TitleImage = titleImageName;
            feed.Video = "false";
            feed.Image = ImageFilename;
            feed.UploadTime = DateTime.Now.ToString("HH:mm:ss");
            feed.UploadDate = DateTime.Today.ToString("dd-MM-yyyy");
            linq.News_Feeds.InsertOnSubmit(feed);
            linq.SubmitChanges();

            var images = Server.MapPath("~/Images");

            var file = Path.Combine(images, Path.GetFileName(ImageFilename));
            File.WriteAllBytes(file, ImageContents);

            var file2 = Path.Combine(images, Path.GetFileName(titleImageName));
            File.WriteAllBytes(file2, titleImageContents);
        }
        [WebMethod]
        public List<News_Feed> RETURN_NEWS_FEED_ALL_WEB()
        {
            return (from News_Feed in linq.News_Feeds
                    select News_Feed).ToList();
        }
        [WebMethod]
        public string RETURN_NEWS_FEED_ABOVE_ID_MOBILE(string id)
        {
            List<News_Feed> feedWithId = (from News_Feed in linq.News_Feeds
                                     where News_Feed.Id == Convert.ToInt32(id)
                                     select News_Feed).ToList();
            List<News_Feed> feeds = (from News_Feed in linq.News_Feeds
                                           select News_Feed).ToList();
            int index = 0;
            for (int i = 0; i < feeds.Count; i++)
            {
                if(feeds.ElementAt(i).Id == feedWithId.ElementAt(0).Id)
                {
                    index = i - 1;
                }
            }
            if(feeds.Count > 1 && index > -1)
            {
                return feeds.ElementAt(index).Id + "//.///" + feeds.ElementAt(index).Title + "//.///" + feeds.ElementAt(index).Description + "//.///" + feeds.ElementAt(index).ReadMoreLink + "//.///" + feeds.ElementAt(index).TitleImage + "//.///" + feeds.ElementAt(index).Video + "//.///" + feeds.ElementAt(index).Image + "//.///" + feeds.ElementAt(index).UploadTime + "//.///" + feeds.ElementAt(index).UploadDate;
            }
            else
            {
                return "false";
            }
        }
        [WebMethod]
        public string RETURN_NEWS_FEED_LAST_MOBILE()
        {
            List<News_Feed> feeds = (from News_Feed in linq.News_Feeds
                                    select News_Feed).ToList();
            if(feeds.Count > 0)
            {
                return feeds.ElementAt(feeds.Count - 1).Id + "//.///" + feeds.ElementAt(feeds.Count - 1).Title + "//.///" + feeds.ElementAt(feeds.Count - 1).Description + "//.///" + feeds.ElementAt(feeds.Count - 1).ReadMoreLink + "//.///" + feeds.ElementAt(feeds.Count - 1).TitleImage + "//.///" + feeds.ElementAt(feeds.Count - 1).Video + "//.///" + feeds.ElementAt(feeds.Count - 1).Image + "//.///" + feeds.ElementAt(feeds.Count - 1).UploadTime + "//.///" + feeds.ElementAt(feeds.Count - 1).UploadDate;
            }
            else
            {
                return "false";
            }
        }

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
        public void  DOWNLOAD_FILE()
        {

            HttpContext.Current.Response.Clear();
            HttpContext.Current.Response.ContentType = "application/octet-stream";
            HttpContext.Current.Response.AppendHeader("Content-Disposition", "filename="
                + "harry.jpg");
            HttpContext.Current.Response.TransmitFile(Server.MapPath("~/Videos/")
                + "harry.jpg");
            HttpContext.Current.Response.End();

        }
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
		[WebMethod]
		public string LOGIN(string userName,string password)
		{
			List<User> userTable = (from User in linq.Users
									where User.User_Name == userName && User.Password == password
									select User).ToList();
			if(userTable.Count == 1)
			{
				return userTable.ElementAt(0).Id + "," + userTable.ElementAt(0).User_Name + "," + userTable.ElementAt(0).Password + "," + userTable.ElementAt(0).Email + "," + userTable.ElementAt(0).Model_ID + "," + userTable.ElementAt(0).Model_ID_VN;
			}
			else
			{
				return "false";
			}
		}

		[WebMethod]
		public string INSERT_ACCURACY_USERS(string User_ID, string Prediction_Accuracy_Images, string Validation_Accuracy_Images, string Prediction_Accuracy_VN, string Validation_Accuracy_VN)
		{
			Accuracy_User acc_Table = new Accuracy_User();
			acc_Table.User_ID = Convert.ToUInt16(User_ID);
			acc_Table.Prediction_Accuracy_Images = Math.Round(decimal.Parse(Prediction_Accuracy_Images.Replace(".", ",")), 0).ToString();
			acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
			acc_Table.Prediction_Accuracy_VN = Math.Round(decimal.Parse(Prediction_Accuracy_VN.Replace(".", ",")), 0).ToString();
			acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
			linq.Accuracy_Users.InsertOnSubmit(acc_Table);
			linq.SubmitChanges();
			return "true";
		}
		[WebMethod]
		public string RETURN_ACCURACY_USERS(string User_ID)
		{
			List<Accuracy_User> accur_Table = (from Accuracy_User in linq.Accuracy_Users
											 where Accuracy_User.User_ID == Convert.ToInt16(User_ID)
											select Accuracy_User).ToList();
			if (accur_Table.Count == 1)
			{
				return accur_Table.ElementAt(0).Id + "," + accur_Table.ElementAt(0).User_ID + "," + accur_Table.ElementAt(0).Prediction_Accuracy_Images + "," + accur_Table.ElementAt(0).Validation_Accuracy_Images + "," + accur_Table.ElementAt(0).Prediction_Accuracy_VN + "," + accur_Table.ElementAt(0).Validation_Accuracy_VN;
			}
			else
			{
				return "false";
			}
		}

		[WebMethod]
		public string UPDATE_ACCURACY_USERS(string User_ID, string Prediction_Accuracy_Images, string Validation_Accuracy_Images, string Prediction_Accuracy_VN, string Validation_Accuracy_VN)
		{

			Accuracy_User acc_Table = (from Accuracy_User in linq.Accuracy_Users
									   where Accuracy_User.User_ID == Convert.ToInt16(User_ID)
										select Accuracy_User).First();
			acc_Table.Prediction_Accuracy_Images = Math.Round(decimal.Parse(Prediction_Accuracy_Images.Replace(".", ",")), 0).ToString();
			acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
			acc_Table.Prediction_Accuracy_VN = Math.Round(decimal.Parse(Prediction_Accuracy_VN.Replace(".", ",")), 0).ToString();
			acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
			linq.SubmitChanges();
			return "true";
		}

		[WebMethod]
		public string UPDATE_TRAINING_ACCURACY_USERS(string Model_ID,  string Validation_Accuracy_Images, string Validation_Accuracy_VN)
		{
			List<User> userTable = (from User in linq.Users
									where User.Model_ID == Convert.ToInt32(Model_ID)
                                    select User).ToList();
            if (userTable.Count > 0)
            {
                
                for (int i = 0; i < userTable.Count; i++)
                {
                    Accuracy_User acc_Table = (from Accuracy_User in linq.Accuracy_Users
                                               where Accuracy_User.User_ID == userTable.ElementAt(i).Id
                                               select Accuracy_User).First();
                    acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
                    acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
                    linq.SubmitChanges();
                }
                return "true";
            }
            else
            {
                return "false";
            }

			
		}

		[WebMethod]
		public string UPDATE_TRAINING_ACCURACY_USERS_VN(string Model_ID_VN, string Validation_Accuracy_Images, string Validation_Accuracy_VN)
		{
			List<User> userTable = (from User in linq.Users
									where User.Model_ID_VN == Convert.ToInt32(Model_ID_VN)
									select User).ToList();
            if(userTable.Count > 0)
            {
                for (int i = 0; i < userTable.Count; i++)
                {
                    Accuracy_User acc_Table = (from Accuracy_User in linq.Accuracy_Users
                                               where Accuracy_User.User_ID == userTable.ElementAt(i).Id
                                               select Accuracy_User).First();
                    acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
                    acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
                    linq.SubmitChanges();
                }
                return "true";
            }
            else
            {
                return "false";
            }

        
			
		}
        [WebMethod]
        public string INSERT_ACCURACY_USERS_FIRST_VERSION(string User_ID, string Prediction_Accuracy_Images, string Validation_Accuracy_Images, string Prediction_Accuracy_VN, string Validation_Accuracy_VN)
        {
            Accuracy_Users_First_Version acc_Table = new Accuracy_Users_First_Version();
            acc_Table.User_ID = Convert.ToUInt16(User_ID);
            acc_Table.Prediction_Accuracy_Images = Math.Round(decimal.Parse(Prediction_Accuracy_Images.Replace(".", ",")), 0).ToString();
            acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
            acc_Table.Prediction_Accuracy_VN = Math.Round(decimal.Parse(Prediction_Accuracy_VN.Replace(".", ",")), 0).ToString();
            acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
            linq.Accuracy_Users_First_Versions.InsertOnSubmit(acc_Table);
            linq.SubmitChanges();
            return "true";
        }
        [WebMethod]
        public string RETURN_ACCURACY_USERS_FIRST_VERSION(string User_ID)
        {
            List<Accuracy_Users_First_Version> accur_Table = (from Accuracy_Users_First_Version in linq.Accuracy_Users_First_Versions
                                                              where Accuracy_Users_First_Version.User_ID == Convert.ToInt16(User_ID)
                                               select Accuracy_Users_First_Version).ToList();
            if (accur_Table.Count == 1)
            {
                return accur_Table.ElementAt(0).Id + "," + accur_Table.ElementAt(0).User_ID + "," + accur_Table.ElementAt(0).Prediction_Accuracy_Images + "," + accur_Table.ElementAt(0).Validation_Accuracy_Images + "," + accur_Table.ElementAt(0).Prediction_Accuracy_VN + "," + accur_Table.ElementAt(0).Validation_Accuracy_VN;
            }
            else
            {
                return "false";
            }
        }

        [WebMethod]
        public string UPDATE_ACCURACY_USERS_FIRST_VERSION(string User_ID, string Prediction_Accuracy_Images, string Validation_Accuracy_Images, string Prediction_Accuracy_VN, string Validation_Accuracy_VN)
        {

            Accuracy_Users_First_Version acc_Table = (from Accuracy_Users_First_Version in linq.Accuracy_Users_First_Versions
                                                      where Accuracy_Users_First_Version.User_ID == Convert.ToInt16(User_ID)
                                                       select Accuracy_Users_First_Version).First();
            acc_Table.Prediction_Accuracy_Images = Math.Round(decimal.Parse(Prediction_Accuracy_Images.Replace(".", ",")), 0).ToString();
            acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
            acc_Table.Prediction_Accuracy_VN = Math.Round(decimal.Parse(Prediction_Accuracy_VN.Replace(".", ",")), 0).ToString();
            acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
            linq.SubmitChanges();
            return "true";
        }

        [WebMethod]
        public string UPDATE_TRAINING_ACCURACY_USERS_FIRST_VERSION(string Model_ID, string Validation_Accuracy_Images, string Validation_Accuracy_VN)
        {
            List<User> userTable = (from User in linq.Users
                                    where User.Model_ID == Convert.ToInt32(Model_ID)
                                    select User).ToList();
            if (userTable.Count > 0)
            {

                for (int i = 0; i < userTable.Count; i++)
                {
                    Accuracy_Users_First_Version acc_Table = (from Accuracy_Users_First_Version in linq.Accuracy_Users_First_Versions
                                                              where Accuracy_Users_First_Version.User_ID == userTable.ElementAt(i).Id
                                                               select Accuracy_Users_First_Version).First();
                    acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
                    acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
                    linq.SubmitChanges();
                }
                return "true";
            }
            else
            {
                return "false";
            }


        }

        [WebMethod]
        public string UPDATE_TRAINING_ACCURACY_USERS_VN_FIRST_VERSION(string Model_ID_VN, string Validation_Accuracy_Images, string Validation_Accuracy_VN)
        {
            List<User> userTable = (from User in linq.Users
                                    where User.Model_ID_VN == Convert.ToInt32(Model_ID_VN)
                                    select User).ToList();
            if (userTable.Count > 0)
            {
                for (int i = 0; i < userTable.Count; i++)
                {
                    Accuracy_Users_First_Version acc_Table = (from Accuracy_Users_First_Version in linq.Accuracy_Users_First_Versions
                                                              where Accuracy_Users_First_Version.User_ID == userTable.ElementAt(i).Id
                                                              select Accuracy_Users_First_Version).First();
                    acc_Table.Validation_Accuracy_Images = Math.Round(decimal.Parse(Validation_Accuracy_Images.Replace(".", ",")), 0).ToString();
                    acc_Table.Validation_Accuracy_VN = Math.Round(decimal.Parse(Validation_Accuracy_VN.Replace(".", ",")), 0).ToString();
                    linq.SubmitChanges();
                }
                return "true";
            }
            else
            {
                return "false";
            }



        }
        [WebMethod]
		public string INSERT_DEVICE_MAC(string User_ID, string Device_Mac, string Current_Number)
		{
			List<Devices_Mac> macs = (from Devices_Mac in linq.Devices_Macs
									  where Devices_Mac.Mac_Address == Device_Mac
									  select Devices_Mac).ToList();
			if(macs.Count == 0)
			{
				Devices_Mac mac = new Devices_Mac();
				mac.Mac_Address = Device_Mac;
				mac.User_ID = Convert.ToInt32(User_ID);
                mac.Current_Number = Current_Number;
                linq.Devices_Macs.InsertOnSubmit(mac);
				linq.SubmitChanges();
			}
			else
			{
				return "false";
			}
			return "true";
		}
		[WebMethod]
		public string RETURN_DEVICE_MAC(string Device_Mac)
		{
			List<Devices_Mac> macs = (from Devices_Mac in linq.Devices_Macs
									  where Devices_Mac.Mac_Address == Device_Mac
									  select Devices_Mac).ToList();
			if(macs.Count == 0)
			{
				return "false";
			}
			else
			{
				return macs.ElementAt(0).Id + "," + macs.ElementAt(0).Mac_Address + "," + macs.ElementAt(0).User_ID + "," + macs.ElementAt(0).Current_Number;
            }
		}


		//IMAGE PROCESSING 
		[WebMethod]
		public string INSERT_NUM_IMAGES(string num_Images)
		{
			List<Current_Num_Image> num = (from Current_Num_Image in linq.Current_Num_Images
										   select Current_Num_Image).ToList();
			if(num.Count == 0)
			{
				Current_Num_Image num_tab = new Current_Num_Image();
				num_tab.Number = Convert.ToInt32(num_Images);
				linq.Current_Num_Images.InsertOnSubmit(num_tab);
				linq.SubmitChanges();
				return "true";
			}
			return "false";
		}
		[WebMethod]
		public string UPDATE_NUM_IMAGES(string Incr_Num)
		{
			List<Current_Num_Image> num = (from Current_Num_Image in linq.Current_Num_Images
										   select Current_Num_Image).ToList();
			Current_Num_Image new_Num = (from Current_Num_Image in linq.Current_Num_Images
										 where Current_Num_Image.Id == num.ElementAt(0).Id
										 select Current_Num_Image).First();
			new_Num.Number = num.ElementAt(0).Number + Convert.ToInt32(Incr_Num);
			linq.SubmitChanges();
			return "true";
		}
		[WebMethod]
		public string RETURN_CURRENT_NUM_IMAGES()
		{
			List<Current_Num_Image> num = (from Current_Num_Image in linq.Current_Num_Images
										   select Current_Num_Image).ToList();
			return num.ElementAt(0).Id + "," + num.ElementAt(0).Number;
		}
		[WebMethod]
		public string INSERT_IMAGE(string user_ID, string image_Path,string model_Version)
		{

			Image image_Table = new Image();
			image_Table.User_ID = Convert.ToUInt16(user_ID);
			image_Table.Image_Path = image_Path;
			image_Table.Model_Version = model_Version;
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
			string user_string = user.Id + "," + user.User_Name + "," + user.Model_ID + "," + user.Model_ID_VN;
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
				images_arr[i] = images.ElementAt(i).Id + "," + images.ElementAt(i).Image_Path + "," + images.ElementAt(i).User_ID + "," + images.ElementAt(i).Model_Version;
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

		[WebMethod]
		public string[] RETURN_VNS_FOR_MOBILE()
		{
			List<Voice_Note> vns = (from Voice_Note in linq.Voice_Notes
									   select Voice_Note).ToList();
			string[] vns_arr = new string[vns.Count];
			for (int i = 0; i < vns.Count; i++)
			{
				vns_arr[i] = vns.ElementAt(i).Id + "," + vns.ElementAt(i).Voice_Note_Path + "," + vns.ElementAt(i).User_ID;
			}
			return vns_arr;
		}


	}

}

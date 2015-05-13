CREATE DATABASE  IF NOT EXISTS `ezegarraDB` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ezegarraDB`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: ezegarraDB
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `qa4_options`
--

DROP TABLE IF EXISTS `qa4_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa4_options` (
  `title` varchar(40) NOT NULL,
  `content` varchar(8000) NOT NULL,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa4_options`
--

LOCK TABLES `qa4_options` WRITE;
/*!40000 ALTER TABLE `qa4_options` DISABLE KEYS */;
INSERT INTO `qa4_options` VALUES ('allow_change_usernames','1'),('allow_close_questions','1'),('allow_login_email_only','0'),('allow_multi_answers','1'),('allow_private_messages','1'),('allow_self_answer','1'),('allow_user_walls','0'),('allow_view_q_bots','1'),('approve_user_required',''),('avatar_allow_gravatar','1'),('avatar_allow_upload','1'),('avatar_default_blobid',''),('avatar_default_height',''),('avatar_default_show','0'),('avatar_default_width',''),('avatar_message_list_size','20'),('avatar_profile_size','200'),('avatar_q_list_size','0'),('avatar_q_page_a_size','40'),('avatar_q_page_c_size','20'),('avatar_q_page_q_size','50'),('avatar_store_size','400'),('avatar_users_size','30'),('block_bad_words',''),('block_ips_write',''),('cache_acount','3374'),('cache_ccount','0'),('cache_flaggedcount',''),('cache_qcount','500'),('cache_queuedcount',''),('cache_tagcount','504'),('cache_uapprovecount','1'),('cache_unaqcount','4'),('cache_unselqcount','71'),('cache_unupaqcount','500'),('cache_userpointscount','2'),('captcha_module','reCAPTCHA'),('captcha_on_anon_post','1'),('captcha_on_feedback','1'),('captcha_on_register','1'),('captcha_on_reset_password','1'),('captcha_on_unapproved',''),('captcha_on_unconfirmed','0'),('columns_tags','3'),('columns_users','2'),('comment_on_as','0'),('comment_on_qs','0'),('confirm_user_emails','1'),('confirm_user_required',''),('custom_answer',''),('custom_ask',''),('custom_comment',''),('custom_footer',''),('custom_header',''),('custom_home_content',''),('custom_home_heading',''),('custom_in_head',''),('custom_register',''),('custom_sidepanel',''),('custom_welcome',''),('db_version','56'),('do_ask_check_qs','0'),('do_close_on_select','0'),('do_complete_tags','1'),('do_count_q_views','1'),('do_example_tags','1'),('editor_for_as','WYSIWYG Editor'),('editor_for_cs',''),('editor_for_qs','WYSIWYG Editor'),('email_privacy','Privacy: Your email address will not be shared or sold to third parties.'),('event_logger_directory',''),('event_logger_hide_header','1'),('event_logger_to_database','1'),('event_logger_to_files','1'),('extra_field_active','0'),('extra_field_display','0'),('extra_field_label',''),('extra_field_prompt',''),('facebook_app_id',''),('feedback_email','ezegarra@cs.pitt.edu'),('feedback_enabled','1'),('feed_for_activity','1'),('feed_for_hot',''),('feed_for_qa','1'),('feed_for_questions','1'),('feed_for_search',''),('feed_for_tag_qs',''),('feed_for_unanswered','1'),('feed_full_text','1'),('feed_number_items','50'),('feed_per_category','1'),('flagging_hide_after','5'),('flagging_notify_every','2'),('flagging_notify_first','1'),('flagging_of_posts','1'),('follow_on_as','1'),('form_security_salt','dbty48g23unutj2kklicw6cw6t73zl8b'),('from_email','ezegarra@cs.pitt.edu'),('home_description',''),('hot_weight_answers','100'),('hot_weight_a_age','100'),('hot_weight_q_age','100'),('hot_weight_views','100'),('hot_weight_votes','100'),('links_in_new_window','0'),('logo_height',''),('logo_show',''),('logo_url',''),('logo_width',''),('mailing_body','\n\n\n--\nIntranet Q&A\nhttp://intranet.cs.pitt.edu/~ezegarra/microprobe4/'),('mailing_enabled',''),('mailing_from_email','no-reply@intranet.cs.pitt.edu'),('mailing_from_name','Intranet Q&A'),('mailing_last_userid',''),('mailing_per_minute','500'),('mailing_subject','A message from Intranet Q&A'),('match_ask_check_qs','3'),('match_example_tags','3'),('match_related_qs','3'),('max_copy_user_updates','10'),('max_len_q_title','120'),('max_num_q_tags','5'),('max_rate_ip_as','50'),('max_rate_ip_cs','40'),('max_rate_ip_flags','10'),('max_rate_ip_logins','20'),('max_rate_ip_messages','10'),('max_rate_ip_qs','20'),('max_rate_ip_registers','5'),('max_rate_ip_uploads','20'),('max_rate_ip_votes','600'),('max_rate_user_as','25'),('max_rate_user_cs','20'),('max_rate_user_flags','5'),('max_rate_user_messages','5'),('max_rate_user_qs','10'),('max_rate_user_uploads','10'),('max_rate_user_votes','300'),('max_store_user_updates','50'),('min_len_a_content','12'),('min_len_c_content','12'),('min_len_q_content','0'),('min_len_q_title','12'),('min_num_q_tags','0'),('moderate_anon_post',''),('moderate_by_points',''),('moderate_edited_again',''),('moderate_notify_admin','1'),('moderate_points_limit','150'),('moderate_unapproved',''),('moderate_unconfirmed',''),('moderate_update_time','1'),('moderate_users',''),('mouseover_content_on',''),('nav_activity','0'),('nav_ask','1'),('nav_categories','0'),('nav_home',''),('nav_hot','0'),('nav_qa_is_home','0'),('nav_questions','1'),('nav_tags','1'),('nav_unanswered','1'),('nav_users','0'),('neat_urls','4'),('notice_visitor',''),('notice_welcome',''),('notify_admin_q_post','1'),('notify_users_default','1'),('pages_prev_next','3'),('page_size_activity','20'),('page_size_ask_check_qs','5'),('page_size_ask_tags','5'),('page_size_home','20'),('page_size_hot_qs','20'),('page_size_qs','20'),('page_size_q_as','10'),('page_size_related_qs','5'),('page_size_search','10'),('page_size_tags','30'),('page_size_tag_qs','20'),('page_size_una_qs','20'),('page_size_users','20'),('page_size_wall','10'),('permit_anon_view_ips','70'),('permit_anon_view_ips_points',''),('permit_close_q','70'),('permit_close_q_points',''),('permit_delete_hidden','40'),('permit_delete_hidden_points',''),('permit_edit_a','100'),('permit_edit_a_points',''),('permit_edit_c','70'),('permit_edit_c_points',''),('permit_edit_q','70'),('permit_edit_q_points',''),('permit_edit_silent','40'),('permit_edit_silent_points',''),('permit_flag','110'),('permit_flag_points',''),('permit_hide_show','70'),('permit_hide_show_points',''),('permit_moderate','100'),('permit_moderate_points',''),('permit_post_a','150'),('permit_post_a_points',''),('permit_post_c','150'),('permit_post_c_points',''),('permit_post_q','150'),('permit_post_q_points',''),('permit_post_wall','110'),('permit_post_wall_points',''),('permit_retag_cat','70'),('permit_retag_cat_points',''),('permit_select_a','100'),('permit_select_a_points',''),('permit_view_q_page','150'),('permit_view_voters_flaggers','20'),('permit_view_voters_flaggers_points',''),('permit_vote_a','120'),('permit_vote_a_points',''),('permit_vote_down','120'),('permit_vote_down_points',''),('permit_vote_q','120'),('permit_vote_q_points',''),('points_a_selected','30'),('points_a_voted_max_gain','20'),('points_a_voted_max_loss','5'),('points_base','100'),('points_multiple','10'),('points_per_a_voted',''),('points_per_a_voted_down','2'),('points_per_a_voted_up','2'),('points_per_q_voted',''),('points_per_q_voted_down','1'),('points_per_q_voted_up','1'),('points_post_a','4'),('points_post_q','2'),('points_q_voted_max_gain','10'),('points_q_voted_max_loss','3'),('points_select_a','3'),('points_to_titles',''),('points_vote_down_a','1'),('points_vote_down_q','1'),('points_vote_on_a',''),('points_vote_on_q',''),('points_vote_up_a','1'),('points_vote_up_q','1'),('q_urls_remove_accents','0'),('q_urls_title_length','50'),('recaptcha_public_key',''),('register_notify_admin',''),('search_module',''),('show_a_c_links','1'),('show_a_form_immediate','if_no_as'),('show_custom_answer','0'),('show_custom_ask','0'),('show_custom_comment','0'),('show_custom_footer',''),('show_custom_header',''),('show_custom_home',''),('show_custom_in_head',''),('show_custom_register','0'),('show_custom_sidebar','1'),('show_custom_sidepanel',''),('show_custom_welcome','0'),('show_c_reply_buttons','1'),('show_fewer_cs_count','5'),('show_fewer_cs_from','10'),('show_full_date_days','7'),('show_home_description',''),('show_message_history','1'),('show_notice_visitor','0'),('show_notice_welcome','0'),('show_selected_first','1'),('show_url_links','1'),('show_user_points','0'),('show_user_titles','1'),('show_view_counts','1'),('show_view_count_q_page','1'),('show_when_created','1'),('site_language',''),('site_maintenance','0'),('site_theme','Classic'),('site_theme_mobile','Snow'),('site_title','MicroProbe Q&A'),('site_url','http://intranet.cs.pitt.edu/~ezegarra/microprobe4/'),('smtp_active','0'),('smtp_address',''),('smtp_authenticate','0'),('smtp_password','peru1995'),('smtp_port','25'),('smtp_secure',''),('smtp_username','ezegarra'),('sort_answers_by','created'),('suspend_register_users',''),('tags_or_categories','t'),('tag_separator_comma','0'),('votes_separated','0'),('voting_on_as','0'),('voting_on_qs','0'),('voting_on_q_page_only','0'),('wysiwyg_editor_upload_images','');
/*!40000 ALTER TABLE `qa4_options` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:19:22

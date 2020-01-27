#include <gst/gst.h>

int
main (int argc, char *argv[])
{
	
	
  //GstElement *pipeline, *source, *capsfilter, *mulawenc, *rtppcmupay, *sink;
  GstBus *bus;
  GstMessage *msg;
  GstStateChangeReturn ret;
  
  GstPipeline *pipeline;
  //GstPipeline *src1;
  GError *error = NULL;


  /* Initialize GStreamer */
  gst_init (&argc, &argv);
  
  pipeline = GST_PIPELINE (gst_parse_launch ("audiotestsrc ! mulawenc ! rtppcmupay ! udpsink host=192.168.1.101 port=5001", &error));
  

  /* Create the elements 
  source = gst_element_factory_make ("audiotestsrc", "source");
  capsfilter = gst_element_factory_make("capsfilter", "camera_caps");
  mulawenc = gst_element_factory_make ("mulawenc", "mulawenc");
  rtppcmupay = gst_element_factory_make ("rtppcmupay", "rtppcmupay");
  sink = gst_element_factory_make ("udpsink", "sink");
  */
  
  //pipeline = gst_pipeline_new ("test-pipeline");
  //pipe = gst_parse_launch("audiotestsrc ! mulawenc ! rtppcmupay ! udpsink host=192.168.1.101 port=5001", &error); 
/* thi
streamer.c:28:106: warning: passing argument 2 of ‘gst_parse_launch’ from incompatible pointer type [-Wincompatible-pointer-types]
   Create the empty pipeline 
  pipeline = gst_pipeline_new ("test-pipeline");

  if (!pipeline || !source || !capsfilter || !mulawenc || !rtppcmupay || !sink) {
    g_printerr ("Not all elements could be created.\n");
    return -1;
  }

   Build the pipeline 
  gst_bin_add_many (GST_BIN (pipeline), source, capsfilter, mulawenc, rtppcmupay, sink, NULL);
  if (gst_element_link (source, sink) != TRUE) {
    g_printerr ("Elements could not be linked.\n");
    gst_object_unref (pipeline);
    return -1;
  }
  */

  /* Modify the source's properties */
  //g_object_set (source, "pattern", 0, NULL);
  //g_object_set (sink, "host", "192.168.1.101", "port", 5001, NULL);
   
   
   //g_object_set(G_OBJECT(sink),
   //          "host", "192.168.1.100",
   //          "port", 5000,
   //          NULL);

  /* Start playing */
  ret = gst_element_set_state (GST_ELEMENT (pipeline), GST_STATE_PLAYING);
  if (ret == GST_STATE_CHANGE_FAILURE) {
    g_printerr ("Unable to set the pipeline to the playing state.\n");
    gst_object_unref (pipeline);
    return -1;
  }

  /* Wait until error or EOS */
  bus = gst_element_get_bus (GST_ELEMENT (pipeline));
  msg =
      gst_bus_timed_pop_filtered (bus, GST_CLOCK_TIME_NONE,
      GST_MESSAGE_ERROR | GST_MESSAGE_EOS);

  /* Parse message */
  if (msg != NULL) {
    GError *err;
    gchar *debug_info;

    switch (GST_MESSAGE_TYPE (msg)) {
      case GST_MESSAGE_ERROR:
        gst_message_parse_error (msg, &err, &debug_info);
        g_printerr ("Error received from element %s: %s\n",
            GST_OBJECT_NAME (msg->src), err->message);
        g_printerr ("Debugging information: %s\n",
            debug_info ? debug_info : "none");
        g_clear_error (&err);
        g_free (debug_info);
        break;
      case GST_MESSAGE_EOS:
        g_print ("End-Of-Stream reached.\n");
        break;
      default:
        /* We should not reach here because we only asked for ERRORs and EOS */
        g_printerr ("Unexpected message received.\n");
        break;
    }
    gst_message_unref (msg);
  }

  /* Free resources */
  gst_object_unref (bus);
  gst_element_set_state (GST_ELEMENT (pipeline), GST_STATE_NULL);
  gst_object_unref (pipeline);
  return 0;
}

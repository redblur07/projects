#include <string.h>
#include <jni.h>
#include <gst/gst.h>
#include "HelloJNI.h"   // Generated

char str[] = "hello";
int hey = 3;
int init();
JNIEXPORT jstring JNICALL Java_HelloJNI_nativeInfo
  (JNIEnv *env, jobject obj){
	
	//char buffer[20];
    //scanf("%s", buffer);
    printf("haha");
    hey = init();
    return (*env)->NewStringUTF(env, str);
	
  }
  
  
 int init(){	
	
  //GstElement *pipeline, *source, *capsfilter, *mulawenc, *rtppcmupay, *sink;
  GstBus *bus;
  GstMessage *msg;
  GstStateChangeReturn ret;
  
  GstPipeline *pipeline;
  //GstPipeline *src1;
  GError *error = NULL;


  /* Initialize GStreamer */
  gst_init( 0, 0);
  
  pipeline = GST_PIPELINE (gst_parse_launch ("audiotestsrc ! mulawenc ! rtppcmupay ! udpsink host=192.168.1.101 port=5001", &error));
  
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

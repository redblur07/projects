#include <gst/gst.h>

int main(int argc, char *argv[]) {
  GstPipeline *sink;
  //GstPipeline *src1;
  GError *error = NULL;
  GstStateChangeReturn ret;
  //GstBus *bus;
  //GstMessage *msg;

  /* Initialize GStreamer */
  gst_init (&argc, &argv);

  /* Create one sink and three source pipelines */
  sink = GST_PIPELINE (gst_parse_launch ("audiotestsrc ! mulawenc ! rtppcmupay ! udpsink host=192.168.1.101 port=5001", &error));
  //src1 = GST_PIPELINE (gst_parse_launch ("interpipesrc listen-to=sink ! autovideosink", &error));
  
    ret = gst_element_set_state (GST_ELEMENT (sink), GST_STATE_PLAYING);
  if (ret == GST_STATE_CHANGE_FAILURE) {
    g_printerr ("Unable to set the pipeline to the playing state.\n");
    gst_object_unref (sink);
    return -1;
  }
  
    //bus = gst_element_get_bus (GST_ELEMENT (sink));
    //msg = gst_bus_timed_pop_filtered (bus, GST_CLOCK_TIME_NONE, GST_MESSAGE_ERROR | GST_MESSAGE_EOS);
  
  
  /* Play the pipelines */
  //gst_element_set_state (GST_ELEMENT (sink), GST_STATE_PLAYING);
  //gst_element_set_state (GST_ELEMENT (src1), GST_STATE_PLAYING);
  //gst_element_change_state (GST_ELEMENT (sink), GST_STATE_CHANGE_PAUSED_TO_PLAYING);
  //gst_element_change_state (GST_ELEMENT (src1), GST_STATE_CHANGE_PAUSED_TO_PLAYING);

  //sleep(10);

  /* Stop pipelines */
  //gst_element_change_state (GST_ELEMENT (sink), GST_STATE_CHANGE_READY_TO_NULL);
  //gst_element_change_state (GST_ELEMENT (src1), GST_STATE_CHANGE_READY_TO_NULL);

  /* Cleanup */
  g_object_unref (sink);
  //g_object_unref (src1);

  return 0;
}

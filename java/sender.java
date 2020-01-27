import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Version;
import org.freedesktop.gstreamer.State;


public class sender{

public static void main(String args[]) throws Exception {
	
	
	Gst.init(new Version(1, 14));

	Pipeline pipeline =new Pipeline("pipe");
	
	pipeline = (Pipeline) Gst.parseLaunch(
	"audiotestsrc ! mulawenc ! rtppcmupay ! udpsink host=192.168.1.103 port=5001"
	);		
	
	pipeline.setState(State.PLAYING);
	Gst.main();
    pipeline.setState(State.NULL);
	
}

}

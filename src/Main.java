import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.media.Player;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.ControllerListener;
import javax.media.ControllerEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.PrefetchCompleteEvent;
import javax.media.EndOfMediaEvent;
import java.awt.Component;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
	public static void main(String az[]) {
        new QAMediaPlayer();
	}
}

class QAMediaPlayer {
    public static final short WINDOW_WIDTH = 800;
    public static final short WINDOW_HEIGHT = 600;
    public static final short PLAYER_WIDTH = 640;
    public static final short PLAYER_HEIGHT = 480;
    JFrame jframe;
    JDesktopPane jdp;
    PlayerFrame pf;
    public QAMediaPlayer(){
        jframe = new JFrame("QAMediaPlayer");
        jdp = new JDesktopPane();
        pf = new PlayerFrame();
        jframe.add(jdp);
        jdp.add(pf);
        jframe.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        pf.setLocation(WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2, 
                WINDOW_HEIGHT / 2 - PLAYER_HEIGHT / 2);
        pf.setVisible(true);

    }
}

class PlayerFrame extends JInternalFrame implements ControllerListener {
    // Had to do this as JInternalFrame is a serializable type.
    // Warnings otherwise.
    private static final long serialVersionUID = 42L;
    private Player player;
    private Component visual;
    private Component control;
    private int vid_width, vid_height, cont_height;
    public PlayerFrame(){
        super("", false, false, false, false);
        try {
            File vid = new File("../media/another_movie_2.mov"); 
            String vid_url = "file://" + vid.getCanonicalPath();
            this.setTitle(vid_url);
            URL url = new URL(vid_url);
            player = Manager.createPlayer(url);
            // Have to wait for realize event to complete.
            player.addControllerListener(this);
            player.realize();
            //this.add(visual);
        } catch (MalformedURLException mue){
            System.out.println(mue.toString());
        } catch(IOException ioe){
            System.out.println(ioe.toString());
        } catch(NoPlayerException npe){
            System.out.println(npe.toString());
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void controllerUpdate(ControllerEvent ce){
        if(ce instanceof RealizeCompleteEvent){
            System.out.println("Realize Completed");
            // Initiate prefetch
            player.prefetch();
        } else if (ce instanceof PrefetchCompleteEvent) {
            // Can prepare video for playback
            System.out.println("Prefetch Completed");
            // Get the video file width and height and set this frame
            // to the same dimensions. Also consider the control UI panel.
            if ((visual = player.getVisualComponent()) != null) {
                vid_width = visual.getPreferredSize().width;
                vid_height = visual.getPreferredSize().height;
                add(visual, BorderLayout.CENTER);
            } else vid_width = QAMediaPlayer.PLAYER_WIDTH;
            if ((control = player.getControlPanelComponent()) != null){
                cont_height = control.getPreferredSize().height;
                control.setLocation(QAMediaPlayer.PLAYER_WIDTH / 2, 
                        QAMediaPlayer.PLAYER_HEIGHT - cont_height);
                add(control, BorderLayout.SOUTH);
            }
            // The video is stretched to fit the parent pane dimensions.
            //setSize(vid_width, vid_height + cont_height);
            setSize(QAMediaPlayer.PLAYER_WIDTH, 
                    QAMediaPlayer.PLAYER_HEIGHT);
            // Not sure what this does...
            validate();
            // Start the video.
            player.start();
        } else if (ce instanceof EndOfMediaEvent){
            System.out.println("Video Finished");
        }
    }
}
// realize?
// validate?
// getControlPanelComponent?
// getVisualComponent?
// stop dragging/moving of internal pane

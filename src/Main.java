import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.media.*;
import javax.swing.*;

import java.net.URL;

public class Main {
	public static void main(String[] args) {
        new QAMediaPlayer();
	}
}

/**
 * A simple video player utilizing javax.media.Player.
 * Use the menu bar to open a video file.
 * BUG: Sometimes the video doesn't appear, no exceptions/warnings
 */
class QAMediaPlayer implements ActionListener, ControllerListener {
    private static final short WINDOW_WIDTH = 800;
    private static final short WINDOW_HEIGHT = 600;
    private JFrame jframe;
    private JMenu file;
    private JMenuItem open;
    private JMenuItem exit;
    private Player player;
    private Component videoFrame;
    private Component playerPlaybackControls;
    private JPanel playbackPanel;
    private JButton playBtn;
    private JButton stopBtn;
    QAMediaPlayer(){
        jframe = new JFrame("QAMediaPlayer");
        JMenuBar menuBar = new JMenuBar();
        open = new JMenuItem("Open");
        exit = new JMenuItem("Exit");
        file = new JMenu("File");
        menuBar.add(file);
        file.add(open);
        file.add(exit);
        jframe.setJMenuBar(menuBar);
        open.addActionListener(this);
        exit.addActionListener(this);
        playBtn = new JButton("Play");
        stopBtn = new JButton("Stop");
        playbackPanel = new JPanel();
        playbackPanel.setLayout(new FlowLayout());
        playbackPanel.add(playBtn);
        playbackPanel.add(stopBtn);
        jframe.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == open){
            // show the sample files that will definitely work with the player
            JFileChooser jFileChooser = new JFileChooser("./media");
            int returnVal = jFileChooser.showOpenDialog(jframe);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                playVideo(jFileChooser.getSelectedFile());
            }
        } else if(e.getSource() == exit){
            System.exit(0);
        }
    }

    private void playVideo(File selectedFile) {
        if(player != null){
            destroyPlayer();
            createPlayer(selectedFile);
        } else {
            createPlayer(selectedFile);
        }
    }

    /**
     * Remove the current player instance and associated playback components.
     * Would be wiser to check whether the same file has been passed to the player, in that case just replay the video.
     */
    private void destroyPlayer(){
        player.stop();

        for(Component c : jframe.getContentPane().getComponents()){
            if(c == playbackPanel){
                jframe.remove(c);
            }
            if(c == playerPlaybackControls){
                jframe.remove(playerPlaybackControls);
                playerPlaybackControls = null;
            }
            if(c == videoFrame){
                jframe.remove(videoFrame);
                videoFrame = null;
            }
        }
        player = null;
    }

    private void createPlayer(File selectedFile){
        try {
            jframe.setTitle("QAMediaPlayer: " + selectedFile.getName());
            URL url = new URL("file:///" + selectedFile.getCanonicalPath());
            player = Manager.createPlayer(url);
            player.addControllerListener(this);
            player.realize();
        } catch(IOException | NoPlayerException e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void controllerUpdate(ControllerEvent controllerEvent) {
        if(controllerEvent instanceof RealizeCompleteEvent) player.prefetch();
        else if(controllerEvent instanceof PrefetchCompleteEvent){
            if((videoFrame = player.getVisualComponent()) != null){
                Dimension playerPreferredSize = new Dimension();
                if((playerPlaybackControls = player.getControlPanelComponent()) != null){
                    // Use the player's built in playback controls
                    playerPreferredSize.setSize(videoFrame.getPreferredSize().getWidth(),
                            playerPlaybackControls.getPreferredSize().getHeight() +
                                    videoFrame.getPreferredSize().getHeight());
                    jframe.add(playerPlaybackControls, BorderLayout.SOUTH);
                } else {
                    // Create and add default playback controls
                    playerPreferredSize.setSize(videoFrame.getPreferredSize().getWidth(),
                            50 + videoFrame.getPreferredSize().getHeight());
                    jframe.add(playbackPanel, BorderLayout.SOUTH);
                }
                jframe.setSize(playerPreferredSize);
                jframe.add(videoFrame, BorderLayout.CENTER);
                jframe.validate();
                player.start();
            }
        } else if(controllerEvent instanceof EndOfMediaEvent)
            System.out.println("Video finished");

    }
}
// realize?
// validate?
// getControlPanelComponent?
// getVisualComponent?
// stop dragging/moving of internal pane

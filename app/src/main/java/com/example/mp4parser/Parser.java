package com.example.mp4parser;

import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    public static boolean append() {
        try {
            // 複数の動画を読み込み
            String f1 = Environment.getExternalStorageDirectory() + "/Movies/sample3.mp4";
            String f2 = Environment.getExternalStorageDirectory() + "/Movies/sample4.mp4";

            Movie[] inMovies = new Movie[]{
                    MovieCreator.build(f1),
                    MovieCreator.build(f2)
            };

            // 1つのファイルに結合
            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                }
            }
            Movie result = new Movie();
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }

            // 出力
            Container out = new DefaultMp4Builder().build(result);
            String outputFilePath = Environment.getExternalStorageDirectory() + "/Movies/output.mp4";
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean test() {
        // 複数の動画を読み込み
        String f1 = Environment.getExternalStorageDirectory() + "/Movies/output.mp4";
        String f2 = Environment.getExternalStorageDirectory() + "/Music/sound.m4a";

        Movie audioTrack = null;
        Movie outputVideo = null;
        try {
            outputVideo = MovieCreator.build(f1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            audioTrack = MovieCreator.build(f2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        List<Track> nuTracks = new ArrayList<>();
        for (Track track : outputVideo.getTracks()) {
            if (track.getHandler().equals("vide")) {
                nuTracks.add(track);
            }
        }
        for (Track track : audioTrack.getTracks()) {
            if (track.getHandler().equals("soun")) {
                nuTracks.add(track);
            }
        }

        outputVideo.setTracks(nuTracks);
        Container out = new DefaultMp4Builder().build(outputVideo);
        try {
            String outputFilePath = Environment.getExternalStorageDirectory() + "/Movies/output2.mp4";
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            FileChannel fc = fos.getChannel();
            out.writeContainer(fc);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

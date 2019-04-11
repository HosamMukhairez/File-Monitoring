/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoringfolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileTest implements Runnable {

    static int fileCount;
    static Thread t;
    static File[] existingFiles;
    static int counterCheck = 0;
    List list;
    String path1;
    String path2;

    FileTest(String path1, String path2) {
        this.path1 = path1;
        this.path2 = path2;
    }

    public void run() {
        t = FileBrowser.t;
        Date date = new Date();
        int currentFilecount = 0;
        File f = new File(this.path1);
        File log = new File(this.path2 + "/log.txt");
        BufferedWriter bw = null;
        FileWriter fstream = null;
        boolean status = f.isDirectory();
        File[] currentFiles = (File[]) null;
        if (status) {
            currentFilecount = f.listFiles().length;

            if (counterCheck == 0) {
                existingFiles = f.listFiles();
                fileCount = currentFilecount;
                counterCheck += 1;
            }
        } else {
            f.mkdir();
        }
        if (fileCount != currentFilecount) {
            if (fileCount > currentFilecount) {
                String str = date.toString() + ": Files have been Deleted :" +
                        (fileCount - currentFilecount);
                try {
                    fstream = new FileWriter(log, true);
                    bw = new BufferedWriter(fstream);
                    currentFiles = f.listFiles();
                    bw.newLine();
                    bw.write(str);
                    bw.flush();
                    this.list = compareArray(existingFiles, currentFiles, bw);
                    fileCount = currentFilecount;
                    existingFiles = currentFiles;
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String str1 = date.toString() + ": New files have been Added: " +
                        (currentFilecount - fileCount);
                try {
                    fstream = new FileWriter(log, true);
                    bw = new BufferedWriter(fstream);
                    currentFiles = f.listFiles();
                    bw.newLine();
                    bw.write(str1);
                    bw.flush();
                    this.list = compaireArray(existingFiles, currentFiles, bw);
                    fileCount = currentFilecount;
                    existingFiles = currentFiles;
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Thread.sleep(7000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.run();
    }

    private List compareArray(File[] existingFiles2, File[] currentFiles, BufferedWriter bw) {
        List list = new ArrayList();
        int counter = 0;
        boolean flag = true;
        for (int i = 0; i < existingFiles2.length; ++i) {
            for (int j = 0; j < currentFiles.length; ++j) {
                if (existingFiles2[i].getName().equals(currentFiles[j].getName())) {
                    flag = true;
                    break;
                }
                flag = false;
            }

            if (!(flag)) {
                list.add(existingFiles2[i]);
                ++counter;
            }
        }
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                String str = null;
                if (((File) list.get(i)).isDirectory()) {
                    str = "Folder:" + ((File) list.get(i)).getName();
                } else {
                    System.out.println("File:" + ((File) list.get(i)).getName());
                    str = "File:" + ((File) list.get(i)).getName();
                }
                try {
                    bw.newLine();
                    bw.write(str);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    private List compaireArray(File[] existingFiles2, File[] currentFiles, BufferedWriter bw) {
        List list = new ArrayList();
        int counter = 0;
        boolean flag = true;
        for (int i = 0; i < currentFiles.length; ++i) {
            for (int j = 0; j < existingFiles2.length; ++j) {
                if (currentFiles[i].getName().equals(
                        existingFiles2[j].getName())) {
                    flag = true;
                    break;
                }
                flag = false;
            }

            if (!(flag)) {
                list.add(currentFiles[i]);
                ++counter;
            }
        }
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                String str = null;
                if (((File) list.get(i)).isDirectory()) {
                    str = "New Folder:" + ((File) list.get(i)).getName();
                } else {
                    str = "New File:" + ((File) list.get(i)).getName();
                }
                try {
                    bw.newLine();
                    bw.write(str);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public List getNewFile() {
        return this.list;
    }
}

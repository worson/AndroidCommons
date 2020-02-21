package com.lib.common.io.stream;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


/**
 * 说明:文件操作相关
 *
 * @author wangshengxing  01.29 2020
 */
public class Files {

    public static byte[] readBytes(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 读取文件里面的内容
     */
    public static String readString(String filePath) {
        try {

            File file = new File(filePath);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 修改文件内容（覆盖或者添加）
     *
     * @param path    文件地址
     * @param content 覆盖内容
     * @param append  指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    public static void writeString(String path, String content, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(path, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param buffer   data
     * @param filePath destination file path
     * @param isAppend
     */
    public static void writeFile(String filePath,byte[] buffer,  boolean isAppend) {

        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists())
                file.createNewFile();

            fos = new FileOutputStream(file, isAppend);
            fos.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * byte to file
     */
    public static void wirteBytes(String fileDest,byte[] bytesArray) {
        writeFile(fileDest,bytesArray,false);
    }


    /**
     * 复制单个文件
     *
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件

                String path = newPath.substring(0, newPath.lastIndexOf("/"));
                File file = new File(path);

                if (!file.exists()) {
                    boolean bl = file.mkdirs();
                }
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    boolean bl = newFile.createNewFile();
                }

                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[16 * 1024];   //利好大文件
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //==System.out.println("copyfile:"+bytesum);

                    fs.write(buffer, 0, byteread);
                }
                Streams.closeQuietly(inStream);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Streams.closeQuietly(inStream);
            Streams.closeQuietly(fs);

        }

        return false;
    }



    /**
     * 复制整个文件夹内容
     *
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            //==FileUtilsWrap.setPermissions(newPath, FileUtilsWrap.S_IRWXU | FileUtilsWrap.S_IRWXG | FileUtilsWrap.S_IRWXO, -1, -1);
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    String newFile = newPath + "/" + (temp.getName()).toString();
                    try {
                        input = new FileInputStream(temp);

                        output = new FileOutputStream(newFile);
                        byte[] b = new byte[1024 * 16];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }

                        output.flush();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        Streams.closeQuietly(output);
                        Streams.closeQuietly(input);
                    }
                } else if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Streams.closeQuietly(output);
            Streams.closeQuietly(input);
        }
        return true;
    }

    /**
     * 获取文件的大小，无此文件时返回0
     *
     * @return
     */
    public static long getFileSize(String filename) {
        File file = new File(filename);
        return getFileSize(file);
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (int i = 0; i < fileList.length; i++) {
                        if (fileList[i].isDirectory()) {
                            size = size + getFileSize(fileList[i]);

                        } else {
                            size = size + fileList[i].length();

                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }






    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    public static boolean exists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean canWrite(String path) {
        return (new File(path)).canWrite();
    }

    public static boolean canRead(String path) {
        return (new File(path)).canRead();
    }

    public static boolean createFolder(String folderPath) {
        if (folderPath != null) {
            File folder = new File(folderPath);
            return createFolder(folder);
        } else {
            return false;
        }
    }

    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) {
                return true;
            }

            targetFolder.delete();
        }

        return targetFolder.mkdirs();
    }

    public static boolean createNewFolder(String folderPath) {
        return delFileOrFolder(folderPath) && createFolder(folderPath);
    }

    public static boolean createNewFolder(File targetFolder) {
        return delFileOrFolder(targetFolder) && createFolder(targetFolder);
    }

    public static boolean createFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            return createFile(file);
        } else {
            return false;
        }
    }

    public static boolean createFile(File targetFile) {
        if (targetFile.exists()) {
            if (targetFile.isFile()) {
                return true;
            }

            delFileOrFolder(targetFile);
        }

        try {
            return targetFile.createNewFile();
        } catch (IOException var2) {
            return false;
        }
    }

    public static boolean createNewFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            return createNewFile(file);
        } else {
            return false;
        }
    }

    public static boolean createNewFile(File targetFile) {
        if (targetFile.exists()) {
            delFileOrFolder(targetFile);
        }

        try {
            return targetFile.createNewFile();
        } catch (IOException var2) {
            return false;
        }
    }

    public static boolean delFileOrFolder(String path) {
        return delFileOrFolder(new File(path));
    }

    public static boolean delFileOrFolder(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    File[] var2 = files;
                    int var3 = files.length;

                    for (int var4 = 0; var4 < var3; ++var4) {
                        File sonFile = var2[var4];
                        delFileOrFolder(sonFile);
                    }
                }

                file.delete();
            }
        }
        return true;
    }


    /**
     *
     * @param filePath
     */
    public static void setFile777(String filePath){
        File dirFile = new File(filePath);
        dirFile.setReadable(true, false);
        dirFile.setExecutable(true, false);
        dirFile.setWritable(true, false);
    }


}

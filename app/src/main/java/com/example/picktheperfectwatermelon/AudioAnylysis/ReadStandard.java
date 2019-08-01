package com.example.picktheperfectwatermelon.AudioAnylysis;

import android.os.Environment;

import com.zlw.main.recorderlib.utils.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Locale;

public class ReadStandard {
    private static final String TAG = "yin";
    private String recordDir = String.format(Locale.getDefault(), "%s/Record/com.yin.main/", Environment.getExternalStorageDirectory().getAbsolutePath());
    //private String recordDir = "D:";

    private File f;//源文件
    private File outF;//写出的文件
    private RandomAccessFile rdf;
   // private byte[] standardByte;//源文件转换的字节数组
    private String standardString;//源数据二进制字符串文件
    private String fileredString ;//过滤后的二进制字符串文件
    private int fileSize;//文件大小
    private short fileFormt;//文件格式，1-pcm
    private short num_channels;//1-单声道 2-双声道
    private int sample_rate;//采样率、音频采样级别
    private int byte_rate;//每秒波形的数据量
    private short block_align;//采样帧的大小
    private short bits_per_sample;//采样位数

    public ReadStandard(String fileName) {
        f = new File(recordDir + "/" + fileName + ".wav");
        //f = new File("D:\\"+fileName);
        //readFile();
    }


    //读取wav
    public void readFile() {
        try {
            rdf = new RandomAccessFile(f, "r");
            fileSize = toInt(read(rdf, 4, 4));//文件大小
            fileFormt = toShort(read(rdf, 20, 2));
            num_channels = toShort(read(rdf, 22, 2));
            sample_rate = toInt(read(rdf, 24, 4));
            byte_rate = toInt(read(rdf, 28, 4));
            block_align = toShort(read(rdf, 32, 2));
            bits_per_sample = toShort(read(rdf, 34, 2));
            //standardByte = new byte[fileSize];


            byte [] array = read(rdf, 36, fileSize);
            standardString = Arrays.toString(array);

            fileredString = Arrays.toString(filedData(array,fileSize));
            Logger.i(TAG,"测试度出的数据===文件大小：%s", getFileSize()) ;
            Logger.i(TAG,"测试度出的数据===文件格式：%s",getFileFormt()) ;
            Logger.i(TAG,"测试度出的数据===声道：%s", getNum_channels()) ;
            Logger.i(TAG,"测试度出的数据===采样率：%s", getSample_rate()) ;
            Logger.i(TAG,"测试度出的数据===采样帧的大小：%s", getBlock_align()) ;
            Logger.i(TAG,"测试度出的数据===每秒数据量：%s", getByte_rate()) ;
            Logger.i(TAG,"测试度出的数据===采样位数：%s", getBits_per_sample()) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rdf != null) {
                    rdf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private int toInt(byte[] b) {
        return ((b[3] << 24) + (b[2] << 16) + (b[1] << 8) + (b[0] << 0));
    }

    private short toShort(byte[] b) {
        return (short) ((b[1] << 8) + (b[0] << 0));
    }


    //读取字节数组
    private byte[] read(RandomAccessFile rdf, int pos, int length) {
        byte[] result = new byte[length];
        try {
            rdf.seek(pos);
            for (int i = 0; i < length; i++) {
                result[i] = rdf.readByte();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //过滤文件
    private byte [] filedData(byte [] result,int length){
        byte [] filed = new byte[length/2];
        byte sta = 120;
        int j = 0;//符合要求的个数
        int k = 0;//连续0的个数
        for(int i= 0 ; i < length ; i++){
            if(k > 10){
                k=0;
                filed[j++] = 0;
            }
            if((result[i] < (- sta)) || (result[i] > sta ) ){
                filed[j++] = result[i];
                Logger.i(TAG,"符合条件的字节个数：%s"+j);
            }else {
                //filed[j++] = 0;
                k++;
            }
        }
        return filed;
    }

    public void writeFilteredFile(String outFileName){
        BufferedWriter bw = null;
        try {
            outF = new File(recordDir + "/f_" + outFileName + ".txt");
            bw = new BufferedWriter(new FileWriter(outF, true));
            bw.write(fileredString);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //向sdcard中写入全部录音数据的二进制文件
    public void writeFile(String outFileName) {
        BufferedWriter bw = null;
        try {
            outF = new File(recordDir + "/" + outFileName + ".txt");
            bw = new BufferedWriter(new FileWriter(outF, true));
            bw.write(standardString);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getStandardString() {
        return standardString;
    }

    public int getFileSize() {
        return fileSize;
    }

    public short getFileFormt() {
        return fileFormt;
    }

    public short getNum_channels() {
        return num_channels;
    }

    public int getSample_rate() {
        return sample_rate;
    }

    public int getByte_rate() {
        return byte_rate;
    }

    public short getBlock_align() {
        return block_align;
    }

    public short getBits_per_sample() {
        return bits_per_sample;
    }
}

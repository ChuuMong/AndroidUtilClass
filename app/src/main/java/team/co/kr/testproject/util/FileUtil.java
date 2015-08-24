package team.co.kr.testproject.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileUtil {

	public static File getFile(String dir) {
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;
		File file = new File(rootPath);
		
		return file;
	}
	
	public static File[] getFileList(String dir, FilenameFilter filter) {
		File file = getFile(dir);
		
		if(filter == null) {
			return file.listFiles();
		} else {
			return file.listFiles(filter);
		}
	}
	
	public static void deleteAllFiles(String dir, FilenameFilter filter) {
		try {
			File[] files = getFileList(dir, filter);
			
			if(files != null) {
				for(File file : files) {
					file.delete();
				}
			}
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] deleteAllFiles Exception : [" + e.getMessage() + "]");
		}
	}
	
	public static void printAllFiles(String dir, FilenameFilter filter) {
		File[] files = getFileList(dir, filter);

		if(files != null) {
			for(File file : files) {
				LogUtil.log("[FileUtil] printAllFiles() - file [" + file.getName() + "]");
			}
		}
	}

	public static String makeDir(String dir) {
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;

		try {
			File file = new File(rootPath);

			if (!file.exists()) {
				if (!file.mkdirs()) {
				}
			}
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] makeDir Exception : [" + e.getMessage() + "]");
			rootPath = "";
		}

		return rootPath + "/";
	}

	public static String makeFile(String dir, String fileName) {
		try {
			String path = makeDir(dir);
			
			if("".equals(path)) {
				String absFileName = dir + "/" + fileName;
				File file = new File(absFileName);
				file.createNewFile();
				
				return absFileName;
			}
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] makeFile Exception : [" + e.getMessage() + "]");
		}
		
		return "";
	}

	public static boolean isFileExists(String filePathName) {
		boolean returnValue = false;
		File f = new File(filePathName);

		try {
			if (f.exists()) {
				returnValue = true;
			} else {
				returnValue = false;
			}
			f = null;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			f = null;
		}

		return returnValue;
	}
	
	public static boolean isFileExists(String dir, String fileName) {
		boolean returnValue = false;
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir + "/" + fileName;
		File file = new File(rootPath);
		
		try {
			if(file.exists()) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
		
		return returnValue;
	}
	
	public static String getFileFullPath(String dir, String fileName) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir + "/" + fileName;
	}

	public static void deleteDir(String dir) {
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;
		
		try {
			if(isFileExists(rootPath)) {
				File file = new File(rootPath);
				File[] childFileList = file.listFiles();
				
				for(File childFile : childFileList) {
					if(childFile.isDirectory()) {
						deleteDir(childFile.getAbsolutePath());
					} else {
						childFile.delete();
					}
				}
				
				if(file.delete()) {
					file.delete();
				}
			}
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] deleteDir Exception : [" + e.getMessage() + "]");
		}
	}

    public static void deleteFilesInDir(String dir) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;

        try {
            if(isFileExists(rootPath)) {
                File file = new File(rootPath);
                File[] childFileList = file.listFiles();

                for(File childFile : childFileList) {
                    if(!childFile.isDirectory()) {
						childFile.delete();
                    }
                }
            }
        } catch(Exception e) {
            LogUtil.log(LogUtil.WARN, "[FileUtil] deleteDir Exception : [" + e.getMessage() + "]");
        }
    }

	public static void deleteFile(Context context, String fileName) {
		File file = context.getFileStreamPath(fileName);

		try {
			if(file.exists()) {
				context.deleteFile(fileName);
			}
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] deleteFile Exception : [" + e.getMessage() + "]");
		}
	}

	public static void writeToFile(Context context, String dir, String fileName, byte data[]) {
		if(data == null || fileName == null || fileName.length() == 0)
			return;

		FileOutputStream fos = null;
		
		try {
			String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;
			File file = new File(rootPath, fileName);
			fos = new FileOutputStream(file, true);
			fos.write(data);
			fos.close();
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] writeToFile Exception : [" + e.getMessage() + "]");
			context.deleteFile(fileName);
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch(IOException e) {
				}
			}
		}
	}
	
	public static void writeToFile(Context context, String dir, String fileName, byte data[], int offset, int length) {
		if(data == null || fileName == null || fileName.length() == 0)
			return;

		FileOutputStream fos = null;
		
		try {
			
			String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;
			File file = new File(rootPath, fileName);
			fos = new FileOutputStream(file, true);
			fos.write(data, offset, length);
			fos.close();
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] writeToFile Exception : [" + e.getMessage() + "]");
			context.deleteFile(fileName);
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch(IOException e) {
				}
			}
		}
	}

	public static void writeToFile(Context context, String dirName, String fileName, String data) {
		writeToFile(context, dirName, fileName, data.getBytes());
	}
	
	public static void writeToPrivateFile(Context context, String fileName, byte data[]) {
		if(data == null || fileName == null || fileName.length() == 0)
			return;

		FileOutputStream fos = null;
		
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(data);
			fos.close();
			fos = null;
		} catch(Exception e) {
			LogUtil.log(LogUtil.WARN, "[FileUtil] writeToPrivateFile Exception : [" + e.getMessage() + "]");
			context.deleteFile(fileName);
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch(IOException e) {
				}
			}
		}
	}
	
	public static byte[] readPrivateFile(Context context, String fileName) {
		FileInputStream fis = null;
		byte[] result = null;
		
		try {
			fis = context.openFileInput(fileName);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		if(fis != null) {
			try {
				int size = fis.available();
				result = new byte[size];
				fis.read(result);
			} catch(IOException e) {
				LogUtil.log(LogUtil.WARN, "[FileUtil] readPrivateFile Exception : [" + e.getMessage() + "]");
				e.printStackTrace();
			}
		}
		
		return result;
	}
}

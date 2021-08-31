package com.qdu.qingyun.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qdu.qingyun.entity.Quiz.QuizWhole;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExcelUtil {

    /**
     * 根据文档格式返回相应的文档对象
     */
    public static Workbook readExcel(MultipartFile file, String pattern) {
        // 文档对象
        Workbook workbook = null;
        if (file != null) {
            try {
                // 获取输入流
                InputStream is = file.getInputStream();
                if ("xls".equals(pattern)) {
                    return new HSSFWorkbook(is);
                } else if ("xlsx".equals(pattern)) {
                    return new XSSFWorkbook(is);
                } else {
                    return null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断上传的文件，是否为excel文件
     */

    public static boolean isExcel(MultipartFile file) {
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();
            if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
                return true;
            }
        }
        return false;
    }


    public static QuizWhole getExcelToQuizImportVO(MultipartFile file, String pattern) throws IOException {
        Row row;
        JSONObject quizJson = new JSONObject();
        QuizWhole quiz = new QuizWhole();
        Workbook workbook = readExcel(file, pattern);

        Sheet sheet = workbook.getSheetAt(0);// 工作表
        int lastRowNum = sheet.getLastRowNum();// 获取最后一行序号,从零开始

        XSSFDrawing dp = (XSSFDrawing) sheet.createDrawingPatriarch();
        List<XSSFShape> pics = dp.getShapes();
        HashMap<Integer, String> pictureMap = new HashMap<>();

        //  读取题库的基本信息，标题，介绍，类别
        quiz.setTitle(sheet.getRow(0).getCell(1) == null ? "" : sheet.getRow(0).getCell(1).toString());
        quiz.setDesc(sheet.getRow(1).getCell(1) == null ? "" : sheet.getRow(1).getCell(1).toString());
        quiz.setCateStr(sheet.getRow(2).getCell(1) == null ? "" : sheet.getRow(2).getCell(1).toString());

        // 获取章节名,小节名
        Row rowChap = sheet.getRow(3);
        Row rowSection = sheet.getRow(4);
        for (int rowIndex = 1; rowIndex < rowChap.getPhysicalNumberOfCells(); rowIndex++) {
            if (rowChap.getCell(rowIndex) != null) {
                System.out.println(rowIndex);
                rowChap.getCell(rowIndex).setCellType(CellType.STRING);
                System.out.println(rowChap.getCell(rowIndex).toString());
                String rowSectionStr = rowSection.getCell(rowIndex).toString();
            }
        }


        // 处理图片
        for (XSSFShape picture : pics) {
            XSSFPicture inpPic = (XSSFPicture) picture;
            XSSFClientAnchor clientAnchor = inpPic.getClientAnchor();
            inpPic.getShapeName();
            PictureData pict = inpPic.getPictureData();
            Timestamp timestamp = new Timestamp(new Date().getTime());
            String pictureFileName = "tempDir/" + timestamp.getTime() + "_" + clientAnchor.getRow1() + ".jpg";
            FileOutputStream out = new FileOutputStream(pictureFileName);
            byte[] data = pict.getData();
            out.write(data);
            out.close();
            // 上传图片
            File pictureFile = new File(pictureFileName);
            //  TencentCosUtil.uploadFile(pictureFile, "/quiz", timestamp.getTime() + "_" + clientAnchor.getRow1());
            pictureMap.put(clientAnchor.getRow1(), pictureFileName);
        }

        JSONArray quesList = new JSONArray();

        // 获取各个题目
        for (int i = 6; i <= lastRowNum; i++) {
            JSONObject ques = new JSONObject();
            row = sheet.getRow(i);

            // 设置单元格为String属性
            for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                if (row.getCell(k) != null) {
                    row.getCell(k).setCellType(CellType.STRING);
                }
            }

            String section = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
            String title = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
            String picture = row.getCell(2) == null ? "-1" : row.getCell(2).getStringCellValue();
            String appendix = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
            String type = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue();
            String answer = row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue();
            String explain = row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue();

            // 匹配章节序号
            String regPattern = "^\\d+=\\d+";
            String chapterIndex = "0";
            String sectionIndex = "0";

            if (section.matches(regPattern)) {
                chapterIndex = section.split("=")[0];
                sectionIndex = section.split("=")[1];
            }

            ques.put("title", title);
            ques.put("answer", answer);
            ques.put("type", type);

            JSONArray optionList = new JSONArray();
//            List<String> optionList = new ArrayList<>();
            if (!picture.equals("无")) {
                if (pictureMap.containsKey(i)) {
                    ques.put("picture", pictureMap.get(i));
                }
            }

            // 处理选项
            for (int j = 6; j < row.getPhysicalNumberOfCells(); j++) {
                row.getCell(j).setCellType(CellType.STRING);
                optionList.add(j - 6, row.getCell(j) == null ? "" : row.getCell(j).toString());
                ques.put("optionArr", optionList);
            }
            quesList.add(ques);
        }
        quizJson.put("quizArr", quesList);
        System.out.println(quizJson);
        return quiz;
    }

    public static void main(String[] args) throws IOException {

    }
}

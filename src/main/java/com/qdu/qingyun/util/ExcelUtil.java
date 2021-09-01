package com.qdu.qingyun.util;

import com.qdu.qingyun.entity.Quiz.*;
import io.netty.util.internal.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtil {

    /**
     * 根据文档格式返回相应的文档对象
     */
    public static Workbook readExcel(MultipartFile file, String pattern) {
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
            return "xls".equals(suffix) || "xlsx".equals(suffix);
        }
        return false;
    }


    public static QuizWhole getExcelToQuizImportVO(MultipartFile file, String pattern) throws IOException {
        Row row;
        QuizWhole quiz = new QuizWhole();
        Workbook workbook = readExcel(file, pattern);
        LinkedList<QuizChapter> quizChapterList = new LinkedList();
        LinkedList<QuizQues> quesList = new LinkedList<>();

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
                QuizChapter quizChapter = new QuizChapter();
                LinkedList<QuizSection> quizSectionList = new LinkedList();
                quizChapter.setTitle(rowChap.getCell(rowIndex).toString());
                rowChap.getCell(rowIndex).setCellType(CellType.STRING);
                String rowSectionStr = rowSection.getCell(rowIndex).toString();
                String[] sectionTitleList = rowSectionStr.split("[;；]");
                for (String sectionTitle : sectionTitleList) {
                    if (!StringUtil.isNullOrEmpty(sectionTitle)) {
                        QuizSection quizSection = new QuizSection();
                        quizSection.setTitle(sectionTitle);
                        quizSectionList.add(quizSection);
                    }
                }
                quizChapter.setQuizSectionList(quizSectionList);
                quizChapterList.add(quizChapter);
            }
        }
        quiz.setChapterList(quizChapterList);

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
            pictureMap.put(clientAnchor.getRow1(), pictureFileName);
        }

        // 获取各个题目
        for (int i = 6; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            QuizQues quizQues = new QuizQues();
            List<QuizFile> fileList = new LinkedList<>();
            LinkedList<QuizOption> options = new LinkedList<>();

            // 设置单元格为String属性
            for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                if (row.getCell(k) != null) {
                    row.getCell(k).setCellType(CellType.STRING);
                }
            }

            String section = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
            String title = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
            String picture = row.getCell(2) == null ? "-1" : row.getCell(2).getStringCellValue();
            String audioFileUrl = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
            String type = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue();
            String answer = row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue();
            String explain = row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue();

            // 匹配章节序号
            String regPattern = "^\\d+=\\d+";
            int chapterIndex = 0;
            int sectionIndex = 0;

            if (section.matches(regPattern)) {
                chapterIndex = Integer.parseInt(section.split("=")[0]);
                sectionIndex = Integer.parseInt(section.split("=")[1]);
            }

            quizQues.setTitle(title);
            quizQues.setAnswerStr(answer);
            quizQues.setTypeStr(type);
            quizQues.setSectionIndex(sectionIndex);
            quizQues.setChapterIndex(chapterIndex);
            quizQues.setExplain(explain);
            if (!audioFileUrl.equals("")) {
                QuizFile audioFile = new QuizFile();
                audioFile.setUrl(audioFileUrl);
                audioFile.setMediaType("voice");
                audioFile.setQuizFileTypeId(1);
                fileList.add(audioFile);
            }
            if (!picture.equals("无")) {
                if (pictureMap.containsKey(i)) {
                    QuizFile picFile = new QuizFile();
                    picFile.setMediaType("img");
                    picFile.setUrl(pictureMap.get(i));
                    picFile.setQuizFileTypeId(1);
                    fileList.add(picFile);
                }
            }

            // 处理选项
            for (int j = 7; j <= row.getLastCellNum(); j++) {
                if (row.getCell(j) != null) {
                    row.getCell(j).setCellType(CellType.STRING);
                    QuizOption option = new QuizOption();
                    option.setBody(row.getCell(j) == null ? "" : row.getCell(j).toString());
                    option.setSeq(j - 7);
                    options.add(option);
                }
            }

            quizQues.setOptions(options);
            quizQues.setFileList(fileList);
            quesList.add(quizQues);
        }

        quiz.setQuesList(quesList);
        return quiz;
    }

    public static void main(String[] args) {

    }
}

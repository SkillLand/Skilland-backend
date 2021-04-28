package com.skilland.game.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
public class FileSystemAccessorTest {

    @Test
    public void getDataFromFolderInResoursesTest(){
        //src/test/recourses/testFolder

        File f = new File("src/test/recourses/testFolder");

        // Populates the array with names of files and directories
        String[]pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
    }

}

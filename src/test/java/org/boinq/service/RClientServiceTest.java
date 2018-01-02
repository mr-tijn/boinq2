package org.boinq.service;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.boinq.Application;
import org.boinq.service.RClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class RClientServiceTest {

	@Inject
	RClientService rClient;

//	currently unused
//	@Test
	public void testVersion() {
		String verString = null;
		Integer ver = 0;
		try {
			verString = rClient.evalForString("R.version$major");
			ver = Integer.parseInt(verString);
		} catch (Throwable t) {
			fail(t.getMessage());
		}
		assertThat(ver).isGreaterThanOrEqualTo(3);
	}

//	currently unused
//	@Test
	public void testSourceExternal() {
		File scriptFile = new File("/tmp/testScript.R");
		FileWriter fw = null;
		try {
			fw = new FileWriter(scriptFile);
			fw.write("helloworld <- function(guy) {\n");
			fw.write("paste('hello',guy)\n");
			fw.write("}");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			fail("could not create test script");
		}
		String response = null;
		try {
			rClient.sourceScript(scriptFile.getAbsolutePath());
			response = rClient.evalForString("helloworld('tester')");
		} catch (Throwable t) {
			fail("could not launch test script");
		}
		assertThat(response).isEqualTo("hello tester");
	}

//	currently unused
//	@Test
	public void testSourceInternal() {
		File scriptFile = new File("/tmp/testScript.R");
		FileWriter fw = null;
		try {
			fw = new FileWriter(scriptFile);
			fw.write("helloworld2 <- function(guy) {\n");
			fw.write("paste('hello',guy)\n");
			fw.write("}");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			fail("could not create test script");
		}
		try {
			rClient.putFile(scriptFile, "localScript.R");
		} catch (Exception e) {
			fail("could not copy script file");
		} 
		String response = null;
		try {
			rClient.sourceScript("localScript.R");
			response = rClient.evalForString("helloworld2('tester')");
		} catch (Throwable t) {
			fail("could not launch test script");
		}
		assertThat(response).isEqualTo("hello tester");
	}

//	currently unused
//	@Test
	public void testgetFile() {
		try {
			rClient.eval("sink('output.txt')");
			rClient.eval("cat('hello\n')");
			rClient.eval("sink()");
		} catch (RserveException e) {
			fail("could not create file on server");
		}
		File targetFile = new File("/tmp/result.txt");
		try {
			rClient.getFile(targetFile, "output.txt");
		} catch (Exception e) {
			fail("could not copy file");
		}
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(targetFile);
		} catch (IOException e) {
			fail("could not read output file");
		}
		assertThat(lines.get(0).trim()).isEqualTo("hello");
	}
	
}

package ru.rik.cardsnew.web;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { ConfigJpaH2.class, WebConfig.class })
@WebAppConfiguration
public class UITest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	private WebClient webClient;

	@Before
	public void init() {
		 mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		 webClient = new WebClient(BrowserVersion.CHROME);
		 webClient.setWebConnection(new MockMvcWebConnection(mockMvc));
		 
		 webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		 webClient.getOptions().setThrowExceptionOnScriptError(false);

	}
// https://habrahabr.ru/post/171911/
//	https://spring.io/blog/2014/05/23/preview-spring-security-test-web-security#user-content-populating-a-test-user-with-a-requestpostprocessor
//	https://spring.io/blog/2014/03/19/introducing-spring-test-mvc-htmlunit
	
//	https://dzone.com/articles/spring-test-thymeleaf-views
//	@Test
	public void getSettings() throws Exception {
		ResultActions result =  mockMvc.perform(get("/"));
                result.andDo(print())
//                .andExpect(view().name("settings"))
//                .andExpect(model().attributeExists("settings"))
//                .andExpect(content().string(containsString("PROFILE_NOT_FOUND_REJECT_CODE")))
                .andExpect(status().isOk());
                
//                MvcResult mvcResult  = result.andReturn();
//                mvcResult.              		 mvcResult.modelAndView.model.contactTypeList.size() == 3
	}
	
//	@Test
//	public void reload() throws Exception {
//		ResultActions result =  mockMvc.perform(get("/reload")
//				.param("phase", "reload"));
//		result.andDo(print())
//		.andExpect(flash().attribute("msgInfo", "Настройки обновлены"))
//		.andExpect(redirectedUrl("/settings"));
//	}
	
//	@Test
	public void useWebClient() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		HtmlPage createMsgFormPage =   webClient.getPage("http://localhost/cards");
		
//		HtmlAnchor htmlAnchor = createMsgFormPage.getAnchorByHref("/reload?phase=reload");
//		HtmlPage page3 = htmlAnchor.click();
		
		
		System.out.println(createMsgFormPage.asText());
		
		
	}
}

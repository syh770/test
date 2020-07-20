package com.sc.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.sc.entity.Message;
import com.sc.entity.XsCustomerFeedback;
import com.sc.mapper.XsCustomerFeedbackMapper;
import com.sc.service.XsCustomerFeedbackService;


@Controller
@RequestMapping("/feedbackctrl")
public class XsFeedbackController {

	//要依赖service 注入进来
	@Autowired
	XsCustomerFeedbackService xsFeedbackService;
	
	@RequestMapping("/selectfeedback.do")
	public ModelAndView selectfeedback(ModelAndView mav,
			@RequestParam(defaultValue="1") Integer pageNum, 
			@RequestParam(defaultValue="10")Integer pageSize,
			XsCustomerFeedback feedback){
		System.out.println("进入查询反馈记录分页方法了"+feedback);
		System.out.println("----"+feedback.getDatemin());
		System.out.println("----"+feedback.getDatemax());
		
		PageInfo<XsCustomerFeedback> page = xsFeedbackService.selectfeedback(pageNum, pageSize, feedback);
		System.out.println();
		mav.addObject("p", page);
		mav.addObject("feedback",feedback);
		mav.setViewName("xs/feedback-list");//视图名称  /WEB-INF/xs/feedback-list.jsp
		return mav;
	}
	
	@RequestMapping("/goaddfeedback.do")
	public ModelAndView goAddFeedback(ModelAndView mav,XsCustomerFeedback feedback){
		System.out.println("进入添加页面"+feedback);
		
	
		if(feedback.getFeedbackRecordId()!=null){
			feedback=xsFeedbackService.getfeedback(feedback.getFeedbackRecordId());
		}
		
		mav.setViewName("xs/feedback-add");
		mav.addObject("feedback",feedback);
		return mav;
	}
	
	@RequestMapping("/addfeedback.do")
	@ResponseBody
	public Message addRecord(ModelAndView mav,XsCustomerFeedback feedback){
		System.out.println("进入添加反馈记录:"+feedback);
		if(feedback.getFeedbackRecordId()!=null){
			xsFeedbackService.updatefeedback(feedback);
		}else{
			xsFeedbackService.addfeedback(feedback);
		}
		
		return new Message("1", "success", "成功");
	}
	
	@RequestMapping("/deletefeedback.do")
	@ResponseBody
	public Message deleteFeedback(ModelAndView mav,XsCustomerFeedback feedback){
		System.out.println("进入删除反馈记录:"+feedback);
		xsFeedbackService.deletefeedback(feedback.getFeedbackRecordId());	
	   return new Message("1", "success", "成功");
	}
	
	@RequestMapping("/deleterecordall.do")
	public String deleteFeedbackAll(ModelAndView mav,long[] ids){
		System.out.println("进入批量删除反馈记录:"+Arrays.toString(ids));
		if(ids!=null&&ids.length>0){
			for (long id : ids) {
				xsFeedbackService.deletefeedback(id);
			}
		}		
		return "redirect:selectfeedback.do";
	}
}

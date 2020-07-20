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
import com.sc.entity.XsSaleDeliveryList;
import com.sc.entity.XsSaleDetail;
import com.sc.service.XsSaleDetailService;
import com.sc.service.XsSaleListService;

@Controller
@RequestMapping("/detailctrl")
public class XsSaleDetailController {
	@Autowired
	XsSaleDetailService  xsDetailService;	
	
	@RequestMapping("/selectdetail.do")
	public ModelAndView selectdetail(ModelAndView mav,
			@RequestParam(defaultValue="1") Integer pageNum, 
			@RequestParam(defaultValue="10")Integer pageSize,
			XsSaleDetail detail){
		System.out.println("进入查询销售单分页方法了"+detail);
		System.out.println("----"+detail.getDatemin());
		System.out.println("----"+detail.getDatemax());	
		PageInfo<XsSaleDetail> page = xsDetailService.selectdetail(pageNum, pageSize, detail);
		System.out.println();
		mav.addObject("p", page);	
        mav.setViewName("xs/saledetail-list");//视图名称  /WEB-INF/xs/detail-list.jsp
		mav.addObject("detail",detail);
		return mav;
	}
	
	@RequestMapping("/goadddetail.do")
	public ModelAndView goAddDetail(ModelAndView mav,XsSaleDetail detail){
		System.out.println("进入添加页面"+detail);
		if(detail.getSalesDetailId()!=null){
			detail=xsDetailService.getdetail(detail.getSalesDetailId());
		}		
		mav.setViewName("xs/saledetail-add");
		mav.addObject("detail",detail);
		return mav;
	}
	
	@RequestMapping("/adddetail.do")
	@ResponseBody
	public Message addDetail(ModelAndView mav,XsSaleDetail detail){
		System.out.println("进入添加销售单:"+detail);
		if(detail.getSalesDetailId()!=null){//如果id不为空就是修改
			xsDetailService.updatedetail(detail);
		}else{
			xsDetailService.adddetail(detail);//否则就是添加  //添加修改共用一个页面
		}		
		return new Message("1", "success", "成功");
	}
	
	@RequestMapping("/deletedetail.do")
	@ResponseBody
	public Message deleteDetail(ModelAndView mav,XsSaleDetail detail){
		System.out.println("进入删除销售单:"+detail);
		xsDetailService.deletedetail(detail.getSalesDetailId());	
	   return new Message("1", "success", "成功");
	}
	
	@RequestMapping("/deletedetailall.do")
	public String deleteDetailAll(ModelAndView mav,long[] ids){
		System.out.println("进入批量删除销售单:"+Arrays.toString(ids));
		if(ids!=null&&ids.length>0){
			for (long id : ids) {
				xsDetailService.deletedetail(id);
			}
		}		
		return "redirect:selectdetail.do";
	}
	
}

package com.myBusiness.products.restaurant.config.job;

//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Calendar;
//import java.util.Date;
//
///**
// * quartz测试单次任务
// */
//@Component
//public class OrderCloseJob implements Job {
//
//    @Autowired
//    Scheduler scheduler;
//
//    public void test(){
//
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.SECOND,10);
//
//        System.out.println("calendar:" + calendar.getTime());
//
//        JobDetail detail = JobBuilder.newJob(OrderCloseJob.class).withIdentity("job1", "group1").build();
//
//
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(calendar.getTime()).build();
//        try {
//            Date date1 = scheduler.scheduleJob(detail, trigger);
//            System.out.println("date1:" + date1);
//        } catch (SchedulerException e) {
//
//
//        }
//    }
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("gogo");
//        System.out.println(scheduler == null);
//    }
//}

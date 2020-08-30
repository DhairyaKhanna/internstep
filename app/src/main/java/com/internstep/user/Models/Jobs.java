package com.internstep.user.Models;

public class Jobs  {
        public String company_logo;
        public String job_name;
        public String applied;
        public String job_description;
        public String more_information;
        public String job_status;
        public String resume;
        public String software;
        public String ques1;
        public String ques2;


        public Jobs(String job_name,String applied,String job_description,String more_information
        ,String job_status,String resume,String software,String ques1,String ques2,String company_logo ){
            this.job_name = job_name;
            this.applied = applied;
            this.job_description = job_description;
            this.more_information = more_information;
            this.job_status = job_status;
            this.resume = resume;
            this.software = software;
            this.ques1 = ques1;
            this.ques2 = ques2;
            this.company_logo = company_logo;
        }

        public Jobs(){

        }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }


    public void setJob_name(String job_name) {
            this.job_name = job_name;
        }

        public void setApplied(String applied) {
            this.applied = applied;
        }

        public void setJob_description(String job_description){
            this.job_description = job_description;
        }

        public void setMore_information(String more_information) {
            this.more_information = more_information;
        }

        public void setJob_status(String job_status) {
            this.job_status = job_status;
        }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setQues2(String ques2) {
        this.ques2 = ques2;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public void setQues1(String ques1) {
            this.ques1 = ques1;
        }


    public String getJob_description() {
        return job_description;
    }

    public String getMore_information() {
        return more_information;
    }

    public String getJob_status() {
        return job_status;
    }

    public String getResume() {
        return resume;
    }

    public String getQues1() {
        return ques1;
    }

    public String getQues2() {
        return ques2;
    }

    public String getSoftware() {
        return software;
    }

    public String getApplied() {
            return applied;
        }

        public String getJob_name() {
            return job_name;
        }


    public String getCompany_logo() {
        return company_logo;
    }
}


import { AxiosInstance } from "axios";
import { UserProfile } from "./model/UserProfile";
import { UserRelateQuestion } from "./model/UserRelateQuestion"
import { Question } from "./model/Question";

export default class UserAPI {
  client: AxiosInstance;
  constructor(client: AxiosInstance) {
    this.client = client
  }

  async profile(): Promise<UserProfile> {
    const rsp = await this.client.get("/api/user/profile")
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }

  async userRelateQuestion(userRelateQuestion: UserRelateQuestion): Promise<Question[]> {
    const rsp = await this.client.post("/api/user/user-related-questions", userRelateQuestion)
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
    }
    
  async createUpdateQuestion(question: Question, tagName: string, isUpdate: string): Promise<String> {
    const rsp = await this.client.post(`/api/user/create-update`, 
    {
      "isUpdate": isUpdate,
      "tagName": tagName,
      "title": question.title,
      "description": question.description,
      "code_content": question.code_content,
      "hint": question.hint,
      "difficulty": question.difficulty,
      "questionID": question.id
    }
    )
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }


  async deleteQuestion(questionID: string): Promise<Question> {
    const rsp = await this.client.delete(`/api/user/${questionID}`)
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    console.log("Delete question id=", questionID)
    return rsp.data
  }
}
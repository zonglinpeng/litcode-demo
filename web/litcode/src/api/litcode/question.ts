import { AxiosInstance } from "axios";
import { Question } from "./model/Question";

export default class QuestionAPI {
  client: AxiosInstance;
  constructor(client: AxiosInstance) {
    this.client = client
  }

  async search(start: Number, step: Number): Promise<Question[]> {
    const rsp = await this.client.get("/api/question/", {
      params: {
        start,
        step,
      }
    })
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }

  async keyWordsSearch(keyWords: string): Promise<Question[]> {
    const rsp = await this.client.get("/api/question/search", {
      params: {
        keyWords,
      }
    })
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }

  async searchID(questionID: string): Promise<Question> {
    const rsp = await this.client.get(`/api/question/${questionID}`)
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }

  async popularTag(): Promise<string> {
    const rsp = await this.client.get("/api/question/popularTag")
    if (rsp.status !== 200) {
      // throw new Error("expect http 200")
      return "none"
    }
    return rsp.data["tag_name"]
  }
  
  async getReactCnt(questionID: string): Promise<Question> {
    const rsp = await this.client.get(`/api/question/${questionID}/reactCnt`)
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }

  // get the reaction status when rendering the QuestionDetail 
  async getReactToQuestion(questionID: string): Promise<string> { 
    const rsp = await this.client.get(`/api/question/${questionID}/react`)
    if (rsp.status !== 200) {
      // throw new Error("expect http 200")
      return "fail"
    }
    return rsp.data["reaction_type"]
  }

  async reactToQuestion(questionID: string, reactType: string, userID: Number): Promise<string> {
    const rsp = await this.client.post(`/api/question/${questionID}/react`,
      {
        "userID": userID,
        "reactType": reactType,
      }
    )
    if (rsp.status !== 200){
      // throw new Error("error in reactToQuestion, expect http 200")
      return "fail"
    }
    return rsp.data
  }

  async deleteLikeQuestion(questionID: string, reactType: string, userID: Number): Promise<string> {
    const rsp = await this.client.delete(`/api/question/${questionID}/react`, {
      data: {
        "userID" : userID,
        "reactType" : reactType,
      }
    })
    if (rsp.status !== 200) {
      // throw new Error("error in reactToQuestion, expect http 200")
      return "fail"
    }
    return rsp.data // the immediate return has no effect from the triggers, so we need a separate call getReactCnt afterwards
  }
  

}
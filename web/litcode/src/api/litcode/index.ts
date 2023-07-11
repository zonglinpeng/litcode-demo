import axios, { AxiosInstance } from "axios";
import CodeRunnerAPI from "./code";
import QuestionAPI from "./question";
import UserAPI from "./user";

class LitcodeAPI {
  client: AxiosInstance;
  private questionInstance;
  private codeRunnerInstance;
  private userInstance;

  constructor(baseURL: string) {
    this.client = axios.create({
      baseURL: baseURL,
    })
    this.questionInstance = new QuestionAPI(this.client)
    this.codeRunnerInstance = new CodeRunnerAPI(this.client)
    this.userInstance = new UserAPI(this.client)
  }

  question(): QuestionAPI {
    return this.questionInstance;
  }

  codeRunner(): CodeRunnerAPI {
    return this.codeRunnerInstance
  }

  user(): UserAPI {
    return this.userInstance
  }
}

const LITCODE_BASE_URL = "/"
const api = new LitcodeAPI(LITCODE_BASE_URL)

export default api;

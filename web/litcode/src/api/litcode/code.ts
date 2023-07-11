import { AxiosInstance } from "axios";
import { CodeRunResult } from "./model/CodeRunResult";

export default class CodeRunnerAPI {
  client: AxiosInstance;
  constructor(client: AxiosInstance) {
    this.client = client
  }

  async run(code: string): Promise<CodeRunResult> {
    const data = new FormData()

    data.append("version", '2')
    data.append("body", code)
    data.append("withVet", "true")

    const rsp = await this.client.post("https://play.golang.org/compile", data)
    if (rsp.status !== 200) {
      throw new Error("expect http 200")
    }
    return rsp.data
  }
}
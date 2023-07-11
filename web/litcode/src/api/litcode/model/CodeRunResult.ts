export interface CodeRunResult {
  Errors: string
  Events: CodeRunResultEvent[]
  Status: number
  IsTest: boolean
  TestsFailed: boolean
  VetOK: boolean
}

interface CodeRunResultEvent {
  Message: string
  Kind: string
  Delay: number
}

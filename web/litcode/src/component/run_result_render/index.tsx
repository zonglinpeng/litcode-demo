
import { useEffect, useState } from 'react';
import './index.css'
import { CodeRunResult } from "../../api/litcode/model/CodeRunResult";
import { CopyBlock } from "react-code-blocks";

interface RunResultRenderProp {
    result: CodeRunResult | null
}

const RunResultRender = (prop: RunResultRenderProp) => {
    const [rst, setRst] = useState<string>("\n")
    useEffect(() => {
        let result = prop.result
        if (result == null) {
            return
        }
        setRst("Waiting for remote server...")

        setTimeout(
            () => {
                setRst("\n")
            },
            500
        )

        let delay = 1000
        let errors = result.Errors
        if (errors.length !== 0) {
            let report = "[Error]\n"
            let lines = result.Errors.split(/\r?\n/)
            for (let line of lines) {
                if (line === "") {
                    continue
                }
                report += line + "\n"
            }
            setTimeout(
                () => {
                    setRst(report)
                },
                delay
            )

            return
        }

        let isTest = result.IsTest
        for (let msg of result.Events) {
            let report = ""
            let lines = msg.Message.split(/\r?\n/)
            for (let line of lines) {
                if (line === "") {
                    continue
                }
                if (isTest) {
                    report += `${line}\n`
                } else {
                    report += `[${msg.Kind}]>>>${line}\n`
                }
            }

            setTimeout(
                () => {
                    setRst((rst) => rst + report)
                },
                delay
            )

            delay += (msg.Delay / 1000000)
        }
        delay += 1000
        if (isTest) {
            let isTestFailed = result.TestsFailed
            if (isTestFailed) {
                let report = "\n[Test]::[Failed]\n"
                setTimeout(
                    () => {
                        setRst((rst) => rst + report)
                    },
                    delay
                )
            } else {
                let report = "\n[Test]::[Passed]\n"
                setTimeout(
                    () => {
                        setRst((rst) => rst + report)
                    },
                    delay
                )
            }
        } else {
            let status = result.Status
            let report = `\n[Main]::[Program exited with code ${status}]\n`
            setTimeout(
                () => {
                    setRst((rst) => rst + report)
                },
                delay
            )
        }
    }, [prop.result])

    return (
        <div className="code-block">
            <CopyBlock
                className="code-block"
                text={rst}
                language="text"
                showLineNumbers={false}
                theme="dracula"
                wrapLines
            />
        </div>
    )
}

export default RunResultRender

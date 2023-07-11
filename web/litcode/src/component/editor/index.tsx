import Editor from "@monaco-editor/react";
import Button from '@mui/material/Button';
import { useEffect, useRef, useState } from 'react';
import api from '../../api/litcode'
import { useParams } from "react-router-dom";
import { Question } from "../../api/litcode/model/Question";
import TextField from "@mui/material/TextField";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import { tagOptions } from "../questions/tags/data";
import './index.css'
import RunResultRender from "../run_result_render";
import { CodeRunResult } from "../../api/litcode/model/CodeRunResult";

const DefaultTestEditorValue =
    `package main

import (
	"testing"
)

func TestAdd(t *testing.T) {
	tests := []struct {
		a    int
		b    int
		want int
	}{
		{a: 1, b: 1, want: 2},
		{a: 2, b: 1, want: 3},
	}
	for _, tt := range tests {
		if got := Add(tt.a, tt.b); got != tt.want {
			t.Errorf("Add(%v, %v) = %v, want %v", tt.a, tt.b, got, tt.want)
		}
	}
}
`

const DefaultStartEditorValue =
    `func Add(a int, b int) int {
	return a + b
}
`

const QuestionEditor = () => {
    const editorStartRef: any = useRef(null)
    const editorTestRef: any = useRef(null)
    const editorDescriptionRef: any = useRef(null)
    const [tag, setTag] = useState<string>("Array")

    let { questionID } = useParams<string>();

    const [q, setQ] = useState<Question>({
        id: "new",
        title: "",
        description: "",
        code_content: DefaultStartEditorValue,
        hint: "",
        difficulty: "Easy",
        likes: 0,
        dislikes: 0,
    })
    const [rst, setRst] = useState<CodeRunResult | null>(null)

    const questionAPI = api.question()
    const userAPI = api.user()

    useEffect(() => {
        (async () => {
            if (questionID !== 'new') {
                let q = await questionAPI.searchID(questionID!)
                console.log(q)
                setQ(q)
            }
        })()
    }, [questionID, questionAPI])

    const onRunClick = async () => {
        const codeRunnerAPI = api.codeRunner()
        const startV = editorStartRef.current.getValue()
        const testV = editorTestRef.current.getValue()
        const fV = testV + "\n" + startV
        const rst = await codeRunnerAPI.run(fV)
        setRst(rst)
    }

    const onTagChange = async (e: any) => {
        const t = e.target.value
        setTag(t)
    }

    const onSaveClick = async () => {
        console.log(q)
        if(q.id=="new"){
            const rst = await userAPI.createUpdateQuestion(q, tag, "false")
        }
        else{
            const rst = await userAPI.createUpdateQuestion(q, tag, "true")
        }
        console.log(rst)

    }

    const onDeleteClick = async () => {
        if (questionID !== 'new') {
            await userAPI.deleteQuestion(questionID!)
        }
        alert(`delete ${questionID} !`)

    }

    const onTitleChange = (e: any) => {
        let title = e.target.value
        setQ(q => {
            return {
                ...q,
                title: title,
            }
        })
    }


    const onDifficultyChange = (e: any) => {
        const v = e.target.value
        setQ(q => {
            return {
                ...q,
                difficulty: v,
            }
        })
    }

    const onDescriptionChange = (value: any, event: any) => {

        setQ(q => {
            return {
                ...q,
                description: value,
            }
        })
    }

    const onCodeChange = (value: any, event: any) => {
        setQ(q => {
            return {
                ...q,
                code_content: value,
            }
        })
    }


    return (
        <div className="editor">
            <div className="editor-title">
                <TextField fullWidth label="Title" variant="outlined" value={q.title} onChange={onTitleChange} />
                <div className="editor-tag">
                    <Select
                        labelId="editor-tag"
                        defaultValue={"Array"}
                        label="Tag"
                        value={tag}
                        onChange={onTagChange}
                    >
                        {
                            tagOptions.map((t) => {
                                return (
                                    <MenuItem value={t.value}>{t.label}</MenuItem>
                                )
                            })
                        }
                    </Select>
                </div>
                <div className="editor-difficulty">
                    <Select
                        labelId="editor-difficulty"
                        defaultValue={"Easy"}
                        label="Difficulty"
                        value={q.difficulty}
                        onChange={onDifficultyChange}
                    >
                        <MenuItem value={"Easy"}>Easy</MenuItem>
                        <MenuItem value={"Medium"}>Medium</MenuItem>
                        <MenuItem value={"Hard"}>Hard</MenuItem>
                    </Select>
                </div>
            </div>
            <div className="editor-main">
                <div className="editor-start">
                    <Editor
                        height="100%"
                        defaultLanguage="go"
                        defaultValue={DefaultStartEditorValue}
                        language="go"
                        theme="light"
                        value={q.code_content}
                        path={"code.go"}
                        onChange={onCodeChange}
                        onMount={(editor, monaco) => { editorStartRef.current = editor }}
                    />
                </div>
                <div className="editor-test">
                    <Editor
                        height="100%"
                        defaultLanguage="golang"
                        defaultValue={DefaultTestEditorValue}
                        language="json"
                        theme="light"
                        path={"test.go"}
                        onMount={(editor, monaco) => { editorTestRef.current = editor }}
                    />
                </div>
                <div className="editor-run-group">
                    <div className="editor-run-result">
                        <RunResultRender result={rst}></RunResultRender>
                    </div>
                    <Button variant="contained" size="large" id="editor-run-button" onClick={onRunClick} >Run</Button>
                    <Button variant="contained" size="large" id="editor-save-button" color="success" onClick={onSaveClick} >Save</Button>
                    <Button variant="contained" size="large" id="editor-delete-button" color="error" onClick={onDeleteClick} >Delete</Button>
                </div>
                <div className="editor-other">
                    <div className="editor-description">
                        <Editor
                            height="100%"
                            defaultLanguage="html"
                            language="html"
                            theme="light"
                            path={"description.html"}
                            value={q.description}
                            onChange={onDescriptionChange}
                            onMount={(editor, monaco) => { editorDescriptionRef.current = editor }}
                        />
                    </div>
                </div>
            </div>
            <div className="editor-other"></div>
        </div>
    )
}

export default QuestionEditor

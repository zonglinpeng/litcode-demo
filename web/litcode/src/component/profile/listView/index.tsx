import { Question } from "../../../api/litcode/model/Question"
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom';

interface ListViewProps {
    data: Question[]
}

const ListView = (prop: ListViewProps) => {
    let navigate = useNavigate()
    const questionsToGridRows = (qs: Question[]) => {
        return qs.map((q) => {
            return {
                id: q.id,
                title: q.title,
                difficulty: q.difficulty,
            }
        })
    }

    const columns: GridColDef[] = [
        { field: 'id', headerName: 'ID', flex: 1, },
        { field: 'title', headerName: 'Title', flex: 1, },
        { field: 'difficulty', headerName: 'Difficulty', flex: 1, },
    ];

    const onClickRow = (e: any) => {
        navigate(`/qeditor/${e.id}`)
    }

    return (
        <DataGrid rows={questionsToGridRows(prop.data)} onRowClick={onClickRow} columns={columns} autoPageSize={false} autoHeight={true} pageSize={10} ></DataGrid>
    )
}

export default ListView
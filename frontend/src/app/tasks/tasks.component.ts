import {Component, OnInit} from '@angular/core';
import {Task} from './task';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Board} from './board';
import {TaskService} from './task.service';
import {MatDialog} from '@angular/material/dialog';
import {DialogTaskDetails} from './dialog-task-details/dialog-task-details';
import {Student} from '../auth/student/student.model';
import {StudentService} from '../auth/student/student.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Project} from '../home/projects/project';
import {ProjectService} from '../home/projects/project.service';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  user: Student;
  project: Project;
  boards: Board[] = [];

  constructor(private studentService: StudentService, private taskService: TaskService, private projectService: ProjectService,
              private dialog: MatDialog, private route: ActivatedRoute, private router: Router) {
    const projectId = this.route.snapshot.paramMap.get('projectId');
    this.initBoards();
    this.getProject(Number(projectId));
    this.getUser();
  }

  ngOnInit(): void {
  }

  initBoards(): void {
    this.boards.push({name: 'TODO', value: 'Do zrobienia', tasks: []});
    this.boards.push({name: 'IN_PROGRESS', value: 'W trakcie', tasks: []});
    this.boards.push({name: 'TESTING', value: 'Testowanie', tasks: []});
    this.boards.push({name: 'DONE', value: 'Zrobione', tasks: []});
  }

  getUser(): void {
    this.studentService.getLoggedUser().subscribe(result => {
      this.user = result;
    }, error => console.log(error));
  }

  getProject(id: number): void {
    this.projectService.getProjectById(id).subscribe(result => {
      this.project = result;
      this.boards.forEach(board => {
        board.tasks = result.tasks.filter(r => r.state === board.name);
      });
    }, error => {
      if (error.status === 404) {
        this.router.navigate(['/not-found']);
      } else {
        console.log(error);
      }
    });
  }

  drop(event: CdkDragDrop<Task[], any>, board: Board): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
      const taskToEdit = event.container.data[event.currentIndex];
      taskToEdit.state = board.name;
      this.taskService.updateTask(taskToEdit).subscribe(result => {
        event.container.data[event.currentIndex] = result;
      }, error => console.log(error));
    }
  }

  openTaskDetails(task: Task): void {
    const dialogRef = this.dialog.open(DialogTaskDetails, {
      width: '50%',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

}

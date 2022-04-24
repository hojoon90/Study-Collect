# 깃 사용전 기본 세팅

## 커밋과 푸시 

Github 기본 세팅시 헤매던 내용들을 정리하여 올려본다.


### 기본 브랜치 변경법
1. 먼저 setting을 클릭해준다.
    ![click_setting](./Images/click_setting.png)
2. 그 후 'Branches' 클릭 후 'Default branch' 안에 있는 양방향으로 엇갈린 화살표를 클릭해준다.
    ![click_branches_set](./Images/click_branches_set.png)
3. 기본으로 사용하고 싶은 브랜치를 선택하고 Update를 클릭해준다.
    ![switch_branch](./Images/switch_branch.png)
4. 경고창이 나오면 그대로 클릭을 해준다.
    ![confirm_click](./Images/confirm_click.png)
5. 기본 브랜치가 master -> main으로 변경된 것을 확인할 수 있다.
    ![change](./Images/change.png)

### local에서 브랜치 삭제 명령어
local 에서 브랜치 삭제는 아래와 같이 **git branch -d {삭제할 브랜치}** 명령어를 통해 삭제해주면 된다.
```
choehojun@choehojuns-MacBook-Air Study-Collect % git branch --list
  main
* master
choehojun@choehojuns-MacBook-Air Study-Collect % git branch -d main
Deleted branch main (was 97bd451).
choehojun@choehojuns-MacBook-Air Study-Collect % git branch --list 
* master
```

### 원격저장소 브랜치 삭제 방법
원격저장소 브랜치 삭제 방법도 크게 다르진 않다.
**git push origin --delete {삭제할 브랜치}** 로 명령어를 입력해주면, 다음과 같이 원격저장소에 있는 브랜치가 삭제된다.
```
choehojun@choehojuns-MacBook-Air Study-Collect % git branch -a
* master
  remotes/origin/main
  remotes/origin/master
choehojun@choehojuns-MacBook-Air Study-Collect % git push origin --delete main
To https://github.com/hojoon90/Study-Collect.git
 - [deleted]         main
choehojun@choehojuns-MacBook-Air Study-Collect % git branch -a                
* master
  remotes/origin/master
```

### 만약 원격저장소 브랜치가 남아있다면...
만약 원격저장소에서는 브랜치를 삭제했는데, 아래처럼 삭제한 원격저장소의 브랜치가 남아있다면, 
**git remote prune origin** 명령어를 이용하여 접근할 수 없는 저장소를 삭제해주면 된다.
prune은 더이상 없는 저장소를 지울 때 사용하는 옵션이다.
```
choehojun@choehojuns-MacBook-Air Study-Collect % git branch -a
* master
  remotes/origin/main
  remotes/origin/master
choehojun@choehojuns-MacBook-Air Study-Collect % git remote prune origin
Pruning origin
URL: https://github.com/hojoon90/Study-Collect.git
 * [pruned] origin/main
choehojun@choehojuns-MacBook-Air Study-Collect % 
```

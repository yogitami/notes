# GIT Commands

GIT is software and GitHub is a service provider
To have git in your folder, run git init (this is a one time thing, it will make sure to track that folder for GIT)

- You can allow head to point to any commit from it's inital position.
- SSH is used with github to validate the user ( You need to SSH in your git and the same key needs to be added to github)

1. Status of the files
: git status

2. See the commit history
: 
    - git log
    - git log --oneline
    - git log -2 (to set the limit)
    - git log --stat
    - git log --since=2.weeks
    - git log --pretty=oneline
    - git log HEAD..origin/develop --oneline (commits in origin but not in our current branch)

3. Create a new branch
: git checkout -b branch_name

4. Git merging
: 
    - git fetch origin, git merge origin/develop **or** git merge origin develop
    - git log HEAD..origin/develop --oneline (commits in origin but not in our current branch)

    - Fast forward merging : Merging the code of your branch into the main branch ( here the main branch doesn't do much)
    - Squash and merge

5. Diff 
: git diff --staged **or** git diff commit_id..commit_id **or** git diff branchName1..branchName2

6. Git stash
:  
    - git stash
    - git stash pop
    - git stash list
    - git stash apply stash_number

7. git checkout hash (detach head)
    **or** git checkout HEAD~2 (look at 2 commits back)

8. git restore file_name (restore to the last commit)

9. git rebase
: 
    - alternative to merging
    - clean up tool (clean up commits)
    - git rebase branch_name

10. git push origin branch_name
    - git pull origin branch_name
    - git push -u origin branch_name
    
11. Add files to staging area
    - git add file_name or -A (for adding all the files)
    - git status

12. Remove files from staging area
    - git reset file_name

13. Viewing the remote project
    - git remote -v
    - git branch -a

14. Undoing a commit
    - git reset HEAD~1 (Reset just one commit back)
    - To reset to multiple commits back : git reset commit_id (This is will unstage all the commits done after this commit)

15. Changing commit message
    - git commit -m "message" --amend
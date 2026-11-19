export const jump = (url) =>{
    let a = document.createElement('a');
    a.href = url;
    // a.target = '_blank';
    a.click();
    a.remove();
}
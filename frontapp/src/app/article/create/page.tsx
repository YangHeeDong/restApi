'use client';

import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Create() {

  const [article, setArticle] = useState({title:"",content:""});
  const router = useRouter();

  const handlerClick = async () => {
    await fetch("http://localhost:8010/api/v1/articles",{
      method:"POST",
      headers:{'Content-Type':"application/json" },
      body:JSON.stringify(article)
    }).then(res => res.json())
    .then(data => {
      alert(data.msg);

      if(data.isSuccess){
        router.push(`/article/${data.data.article.id}`);
      }
    });

  }

  const handlerChange = (e) => {
    const {name, value} = e.target;
    setArticle({...article,[name]:value});
  }

  return (
    <main className="min-h-screen p-24">
      <div>
        <input onChange={handlerChange} name="title" type="text" placeholder="제목을 입력하세요" className="input input-bordered w-full" />
        <textarea onChange={handlerChange} name="content" className="textarea textarea-bordered w-full mt-5" placeholder="내용을 입력하세요"></textarea> 
        
        <button onClick={handlerClick} className="btn w-full mt-3">등록</button>

      </div>
    </main>
  );
}

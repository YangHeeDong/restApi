'use client';

import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Create() {

  const id = useParams().id;

  const [article, setArticle] = useState({id:0,title:"",content:""});
  const router = useRouter();

  const getArticleById = async () => {
    const result = await fetch(`http://localhost:8010/api/v1/articles/${id}`,{method:"GET"}).then(res => res.json())

    if( await result.isFail){
      router.back();
    }
    setArticle(result.data.article);
  }

  useEffect( () => {
    getArticleById();
  },[])

  const handlerClick = async () => {
    await fetch("http://localhost:8010/api/v1/articles",{
      method:"PATCH",
      headers:{'Content-Type':"application/json" },
      body:JSON.stringify(article)
    }).then(res => res.json())
    .then(data => {
      alert(data.msg);

      if(data.isSuccess){
        router.push(`/article/${article.id}`);
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
        <input onChange={handlerChange} name="title" type="text" defaultValue={article.title} className="input input-bordered w-full" />
        <textarea onChange={handlerChange} name="content" className="textarea textarea-bordered w-full mt-5" defaultValue={article.content}></textarea> 
        
        <button onClick={handlerClick} className="btn w-full mt-3">등록</button>

      </div>
    </main>
  );
}

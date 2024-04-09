'use client';

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Detail() {
  const router = useRouter();
  const id = useParams().id;

  const [article, setArticle] = useState({id:"",title:"",content:"",createDate:"",modifiedDate:""});
  
  const getArticleById = async () => {
    const result = await fetch(`http://localhost:8010/api/v1/articles/${id}`,
    {
      method:"GET",
      credentials: 'include' // 클라이언트와 서버가 통신할때 쿠키 값을 공유하겠다는 설정
      
    }).then(res => res.json())

    if( result.isFail ){
      router.back();
    }
    console.log(result)
    setArticle(result.data.article);
  }

  const handlerDelete = async () => {
    if(!confirm("정말로 삭제할 꺼에요?")){
      return;
    }

    const result = await fetch(`http://localhost:8010/api/v1/articles/${id}`,{method:"DELETE"}).then(res => res.json());

    alert(result.msg);

    router.push("/article");
  }

  useEffect( () => {
    getArticleById();
  },[])

  return (
    <main className="min-h-screen p-24">
      <div className="card w-full bg-base-100 shadow-xl">
        <div className="card-body">
          <h2 className="card-title"> {article.id}.  {article.title}</h2>
          <p>{article.content}</p>
          <p>{article.createDate}</p>
          <p>{article.modifiedDate}</p>
        </div>
      </div>
      <div className="flex justify-between mt-5">
        <div>
          <Link href="/article" className="btn">목록</Link>
        </div>
        <div>
          <Link href={"/article/"+article.id+"/edit"} className="btn me-3">수정</Link>
          <button onClick={handlerDelete} className="btn">삭제</button>
        </div>
      </div>
    </main>
  );
}

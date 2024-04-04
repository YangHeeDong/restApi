'use client';

import Link from "next/link";
import { useEffect, useState } from "react";

interface Article{
  id : number,
  title : string,
  content : string,
  createDate : string,
  modifiedDate : string
}

export default function Article() {

  const [articles, setArticles] = useState<Article[]>([]);

  const getArticles = async () => {
    const res = await fetch("http://localhost:8010/api/v1/articles",{method:"GET"}).then(res => res.json())
    const articles = await res.data.articles;
    setArticles(res.data.articles);
    return;
  };

  useEffect( () => {
    getArticles();
    console.log(articles);
  },[]);

  return (
    <main className="min-h-screen p-24">
      <div className="flex">
        <div className="grid grid-cols-4 gap-4 w-full">
          {articles.map( article => 
            (
              <Link href={"/article/"+ article.id } key={article.id}>
                <div className="card bg-base-100 shadow-xl">
                  <div className="card-body">
                    <h2 className="card-title">{article.title}</h2>
                    <p>{article.createDate}</p>
                  </div>
                </div>
              </Link>
            ))
          }
        </div>
      </div>
      <div className="mt-5">
        <Link href="/article/create" className="btn float-end"> 생성 </Link>  
      </div>
    </main>
  );
}

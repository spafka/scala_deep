package com.twitter.bijection
import Bufferable.reallocatingPut
import java.nio.ByteBuffer
import com.twitter.bijection.Inversion.attempt

trait GeneratedTupleBufferable {
  implicit def tuple2[A,B](implicit ba: Bufferable[A], bb: Bufferable[B]):
  Bufferable[(A,B)] = new AbstractBufferable[(A,B)] {
    def put(bytebuf: ByteBuffer, tup: (A,B)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val res = (a, b)
      (bufb, res)
    }
  }
  implicit def tuple3[A,B,C](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C]):
  Bufferable[(A,B,C)] = new AbstractBufferable[(A,B,C)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val res = (a, b, c)
      (bufc, res)
    }
  }
  implicit def tuple4[A,B,C,D](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D]):
  Bufferable[(A,B,C,D)] = new AbstractBufferable[(A,B,C,D)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val res = (a, b, c, d)
      (bufd, res)
    }
  }
  implicit def tuple5[A,B,C,D,E](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E]):
  Bufferable[(A,B,C,D,E)] = new AbstractBufferable[(A,B,C,D,E)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val res = (a, b, c, d, e)
      (bufe, res)
    }
  }
  implicit def tuple6[A,B,C,D,E,F](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F]):
  Bufferable[(A,B,C,D,E,F)] = new AbstractBufferable[(A,B,C,D,E,F)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val res = (a, b, c, d, e, f)
      (buff, res)
    }
  }
  implicit def tuple7[A,B,C,D,E,F,G](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G]):
  Bufferable[(A,B,C,D,E,F,G)] = new AbstractBufferable[(A,B,C,D,E,F,G)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val res = (a, b, c, d, e, f, g)
      (bufg, res)
    }
  }
  implicit def tuple8[A,B,C,D,E,F,G,H](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H]):
  Bufferable[(A,B,C,D,E,F,G,H)] = new AbstractBufferable[(A,B,C,D,E,F,G,H)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val res = (a, b, c, d, e, f, g, h)
      (bufh, res)
    }
  }
  implicit def tuple9[A,B,C,D,E,F,G,H,I](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I]):
  Bufferable[(A,B,C,D,E,F,G,H,I)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val res = (a, b, c, d, e, f, g, h, i)
      (bufi, res)
    }
  }
  implicit def tuple10[A,B,C,D,E,F,G,H,I,J](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val res = (a, b, c, d, e, f, g, h, i, j)
      (bufj, res)
    }
  }
  implicit def tuple11[A,B,C,D,E,F,G,H,I,J,K](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val res = (a, b, c, d, e, f, g, h, i, j, k)
      (bufk, res)
    }
  }
  implicit def tuple12[A,B,C,D,E,F,G,H,I,J,K,L](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l)
      (bufl, res)
    }
  }
  implicit def tuple13[A,B,C,D,E,F,G,H,I,J,K,L,M](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m)
      (bufm, res)
    }
  }
  implicit def tuple14[A,B,C,D,E,F,G,H,I,J,K,L,M,N](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n)
      (bufn, res)
    }
  }
  implicit def tuple15[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
      (bufo, res)
    }
  }
  implicit def tuple16[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
      (bufp, res)
    }
  }
  implicit def tuple17[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
      (bufq, res)
    }
  }
  implicit def tuple18[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q], br: Bufferable[R]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb = reallocatingPut(nextBb) { br.put(_, tup._18) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val (bufr, r) = br.unsafeGet(bufq)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
      (bufr, res)
    }
  }
  implicit def tuple19[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q], br: Bufferable[R], bs: Bufferable[S]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb = reallocatingPut(nextBb) { br.put(_, tup._18) }
      nextBb = reallocatingPut(nextBb) { bs.put(_, tup._19) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val (bufr, r) = br.unsafeGet(bufq)
      val (bufs, s) = bs.unsafeGet(bufr)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)
      (bufs, res)
    }
  }
  implicit def tuple20[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q], br: Bufferable[R], bs: Bufferable[S], bt: Bufferable[T]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb = reallocatingPut(nextBb) { br.put(_, tup._18) }
      nextBb = reallocatingPut(nextBb) { bs.put(_, tup._19) }
      nextBb = reallocatingPut(nextBb) { bt.put(_, tup._20) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val (bufr, r) = br.unsafeGet(bufq)
      val (bufs, s) = bs.unsafeGet(bufr)
      val (buft, t) = bt.unsafeGet(bufs)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
      (buft, res)
    }
  }
  implicit def tuple21[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q], br: Bufferable[R], bs: Bufferable[S], bt: Bufferable[T], bu: Bufferable[U]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb = reallocatingPut(nextBb) { br.put(_, tup._18) }
      nextBb = reallocatingPut(nextBb) { bs.put(_, tup._19) }
      nextBb = reallocatingPut(nextBb) { bt.put(_, tup._20) }
      nextBb = reallocatingPut(nextBb) { bu.put(_, tup._21) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val (bufr, r) = br.unsafeGet(bufq)
      val (bufs, s) = bs.unsafeGet(bufr)
      val (buft, t) = bt.unsafeGet(bufs)
      val (bufu, u) = bu.unsafeGet(buft)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)
      (bufu, res)
    }
  }
  implicit def tuple22[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V](implicit ba: Bufferable[A], bb: Bufferable[B], bc: Bufferable[C], bd: Bufferable[D], be: Bufferable[E], bf: Bufferable[F], bg: Bufferable[G], bh: Bufferable[H], bi: Bufferable[I], bj: Bufferable[J], bk: Bufferable[K], bl: Bufferable[L], bm: Bufferable[M], bn: Bufferable[N], bo: Bufferable[O], bp: Bufferable[P], bq: Bufferable[Q], br: Bufferable[R], bs: Bufferable[S], bt: Bufferable[T], bu: Bufferable[U], bv: Bufferable[V]):
  Bufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)] = new AbstractBufferable[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)] {
    def put(bytebuf: ByteBuffer, tup: (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)) = {
      var nextBb = bytebuf
      nextBb = reallocatingPut(nextBb) { ba.put(_, tup._1) }
      nextBb = reallocatingPut(nextBb) { bb.put(_, tup._2) }
      nextBb = reallocatingPut(nextBb) { bc.put(_, tup._3) }
      nextBb = reallocatingPut(nextBb) { bd.put(_, tup._4) }
      nextBb = reallocatingPut(nextBb) { be.put(_, tup._5) }
      nextBb = reallocatingPut(nextBb) { bf.put(_, tup._6) }
      nextBb = reallocatingPut(nextBb) { bg.put(_, tup._7) }
      nextBb = reallocatingPut(nextBb) { bh.put(_, tup._8) }
      nextBb = reallocatingPut(nextBb) { bi.put(_, tup._9) }
      nextBb = reallocatingPut(nextBb) { bj.put(_, tup._10) }
      nextBb = reallocatingPut(nextBb) { bk.put(_, tup._11) }
      nextBb = reallocatingPut(nextBb) { bl.put(_, tup._12) }
      nextBb = reallocatingPut(nextBb) { bm.put(_, tup._13) }
      nextBb = reallocatingPut(nextBb) { bn.put(_, tup._14) }
      nextBb = reallocatingPut(nextBb) { bo.put(_, tup._15) }
      nextBb = reallocatingPut(nextBb) { bp.put(_, tup._16) }
      nextBb = reallocatingPut(nextBb) { bq.put(_, tup._17) }
      nextBb = reallocatingPut(nextBb) { br.put(_, tup._18) }
      nextBb = reallocatingPut(nextBb) { bs.put(_, tup._19) }
      nextBb = reallocatingPut(nextBb) { bt.put(_, tup._20) }
      nextBb = reallocatingPut(nextBb) { bu.put(_, tup._21) }
      nextBb = reallocatingPut(nextBb) { bv.put(_, tup._22) }
      nextBb
    }
    def get(bytebuf: ByteBuffer) = attempt(bytebuf) { bytebuf =>
      val (bufa, a) = ba.unsafeGet(bytebuf)
      val (bufb, b) = bb.unsafeGet(bufa)
      val (bufc, c) = bc.unsafeGet(bufb)
      val (bufd, d) = bd.unsafeGet(bufc)
      val (bufe, e) = be.unsafeGet(bufd)
      val (buff, f) = bf.unsafeGet(bufe)
      val (bufg, g) = bg.unsafeGet(buff)
      val (bufh, h) = bh.unsafeGet(bufg)
      val (bufi, i) = bi.unsafeGet(bufh)
      val (bufj, j) = bj.unsafeGet(bufi)
      val (bufk, k) = bk.unsafeGet(bufj)
      val (bufl, l) = bl.unsafeGet(bufk)
      val (bufm, m) = bm.unsafeGet(bufl)
      val (bufn, n) = bn.unsafeGet(bufm)
      val (bufo, o) = bo.unsafeGet(bufn)
      val (bufp, p) = bp.unsafeGet(bufo)
      val (bufq, q) = bq.unsafeGet(bufp)
      val (bufr, r) = br.unsafeGet(bufq)
      val (bufs, s) = bs.unsafeGet(bufr)
      val (buft, t) = bt.unsafeGet(bufs)
      val (bufu, u) = bu.unsafeGet(buft)
      val (bufv, v) = bv.unsafeGet(bufu)
      val res = (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)
      (bufv, res)
    }
  }
}
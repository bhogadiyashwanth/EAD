
const Joi=require('joi');
const _ = require('lodash');
const {Forum}=require('../Models/forums');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();

router.post('/addtopic',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let topic =await Forum.findOne({topic:req.body.topic,society_id:req.body.user.address.society_id});
  if (topic) return res.send({msg:'Topic already exists'})

  forum=new Forum({
    topic:req.body.topic,
    creator_name:req.body.user.name,
    is_discussion:req.body.is_discussion,
    society_id:req.body.user.address.society_id,
    description:req.body.description,
  })
  await forum.save()
  let forumDetails={
    msg:"successful",
    forum:forum,
  }
  res.send(forumDetails)
});

router.post('/addcomment',async(req,res)=>{
  const {error} = validateComment(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let forum= await Forum.findById(req.body.topic_id)
  if(!forum) return res.send({msg:"Topic not available"})
  let arr=forum.comments
  arr.push({
    date_created:Date.now(),
    person_name:req.body.user.name,
    comment:req.body.comment,
  })
  forum.comments=arr
  await forum.save()
  return res.send({msg:"successful",forum:forum})
})

router.get('/getTopic',async(req,res)=>{
  const {error} = validategetTopic(req.query);
  if(error) return res.send({msg:error.details[0].message});

  await Forum.find({ society_id: req.query.society_id, is_discussion:true })
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",topics:messages})
    });

})

router.get('/getComplains',async(req,res)=>{
  const {error} = validategetTopic(req.query);
  if(error) return res.send({msg:error.details[0].message});

  await Forum.find({ society_id: req.query.society_id, is_discussion:false })
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",topics:messages})
    });

})

function validate(req){
  const schema={
    topic:Joi.string().required(),
    is_discussion:Joi.boolean(),
    user:Joi.object().required(),
    description:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}

function validateComment(req){
  const schema={
    topic_id:Joi.string().required(),
    comment:Joi.string().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}

function validategetTopic(req){
  const schema={
    flag:Joi.number().required(),
    society_id:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
